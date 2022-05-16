package streaming.source

import streaming.model.LogData

object LogsDataSource:
  val source = Seq(
    LogData("user1", "start", "refB"),
    LogData("user2", "start", "refC"),
    LogData("user3", "start", "refA"),
    LogData("user1", "stop", "refB"),
    LogData("user4", "start", "refA"),
    LogData("user4", "stop", "refA"),
    LogData("user2", "stop", "refC"),
    LogData("user5", "start", "refB"),
    LogData("user5", "stop", "refB")
  )
