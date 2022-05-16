package streaming.source

import streaming.model.ReferenceData

object ReferenceDataSource:
  val source = Seq(
    ReferenceData("refA", "refAFirst"),
    ReferenceData("refB", "refBFirst"),
    ReferenceData("refA", "refASecond"),
    ReferenceData("refC", "refCFirst"),
    ReferenceData("refB", "refBSecond"),
    ReferenceData("refA", "refAThird"),
    ReferenceData("refA", "refAFourth")
  )
