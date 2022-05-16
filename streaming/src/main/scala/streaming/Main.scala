package streaming

import cats.effect.*
import cats.syntax.all.*
import fs2.*
import fs2.concurrent.Topic
import org.typelevel.log4cats.*
import org.typelevel.log4cats.slf4j.*

import scala.concurrent.duration.*

import source.*
import model.*
import exception.DataEnrichingException

object Main extends IOApp.Simple:

  def run: IO[Unit] =

    given logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

    val program = for {
      stateRef <- Ref.of[IO, Map[RefId, ReferenceData]](Map[RefId, ReferenceData]())
      streamer = Streamer.make[IO](LogsDataSource.source, ReferenceDataSource.source)
      processor = Processor.make[IO](streamer, stateRef)
      _ <- logger.info("Starting stream processing app...")
      _ <- processor.processStreams
    } yield ()

    program.recoverWith { case ex: DataEnrichingException =>
      logger.error(ex)("Stream processing failed.")
    }
