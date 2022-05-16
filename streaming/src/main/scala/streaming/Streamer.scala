package streaming

import cats.effect.*
import fs2.*

import scala.concurrent.duration.*

import model.*
import source.*

trait Streamer[F[_]]:
  def logStream: Stream[F, LogData]

  def refStream: Stream[F, ReferenceData]

object Streamer:
  def make[F[_]: Sync: Temporal](
      logsSource: Seq[LogData],
      referenceSource: Seq[ReferenceData]
  ): Streamer[F] =
    new Streamer:
      override lazy val logStream =
        Stream
          .sleep[F](10.seconds)
          .flatMap(_ => Stream.emits(logsSource).meteredStartImmediately(2.seconds))

      override lazy val refStream =
        Stream
          .emits(referenceSource)
          .meteredStartImmediately(5.seconds)
