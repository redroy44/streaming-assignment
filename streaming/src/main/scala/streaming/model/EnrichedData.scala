package streaming.model

final case class EnrichedData(
    userId: UserId,
    action: Action,
    refId: RefId,
    refValue: RefValue
)
