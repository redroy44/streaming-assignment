package streaming.exception

import streaming.model.LogData

import scala.util.control.NoStackTrace

final case class DataEnrichingException(data: LogData) extends NoStackTrace {
  override def getMessage(): String = s"Could not enrich data: $data"
}
