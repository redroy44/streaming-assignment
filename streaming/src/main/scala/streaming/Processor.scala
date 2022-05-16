package streaming

import cats.effect.*
import cats.syntax.all.*
import fs2.RaiseThrowable
import fs2.Stream
import org.typelevel.log4cats.Logger
import retry.RetryDetails
import retry.RetryPolicies.fibonacciBackoff
import retry.RetryPolicies.limitRetriesByCumulativeDelay
import retry.retryingOnAllErrors

import scala.concurrent.duration.*

import model.*
import exception.*

trait Processor[F[_]]:
  def processStreams: F[Unit]

object Processor:
  def make[F[_]: Async: Temporal: RaiseThrowable: Logger](
      streamer: Streamer[F],
      stateRef: Ref[F, Map[RefId, ReferenceData]]
  ): Processor[F] =
    new Processor:

      val retryPolicy = limitRetriesByCumulativeDelay(30.seconds, fibonacciBackoff[F](5.seconds))

      private def onError(ex: Throwable, details: RetryDetails): F[Unit] = {
        Logger[F].warn(s"${ex.getMessage}. Will retry...")
      }

      override def processStreams: F[Unit] =
        Stream(
          updateStream,
          enrichedStream
        ).parJoin(2).compile.drain

      private lazy val updateStream: Stream[F, Unit] =
        streamer.refStream.evalMap { data => updateState(data, stateRef) }

      private lazy val enrichedStream =
        streamer.logStream
          .parEvalMapUnordered(16) { data =>
            retryingOnAllErrors(retryPolicy, onError)(enrichLog(data, stateRef))
          }
          .evalTap(data => Logger[F].info(s"${data}"))

      private def updateState(
          data: ReferenceData,
          stateRef: Ref[F, Map[RefId, ReferenceData]]
      ): F[Unit] = stateRef.update(acc => acc.updated(data.refId, data))

      private def enrichLog(
          data: LogData,
          stateRef: Ref[F, Map[RefId, ReferenceData]]
      ): F[EnrichedData] =
        (for {
          state <- stateRef.get
          _ <- Logger[F].debug(s"data: $data - state: $state")
          enrichedData <- Sync[F].delay(data.toEnrichedData(state(data.refId)))
        } yield enrichedData).adaptError { case ex: NoSuchElementException =>
          DataEnrichingException(data)
        }
