package org.pettswood.specification.concepts

import org.pettswood._

class TimedResults(results: ResultSummary) extends Results(results) {
  override def columns = super.columns orElse {
    case "time" => Expect(results.totalTally.exception)
  }
}