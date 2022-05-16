package streaming.model

final case class LogData(userId: UserId, action: Action, refId: RefId):
  def toEnrichedData(refData: ReferenceData): EnrichedData =
    EnrichedData(userId, action, refId, refData.refValue)
