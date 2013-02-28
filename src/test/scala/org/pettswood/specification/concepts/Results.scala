package org.pettswood.specification.concepts

import org.pettswood._

abstract class Results(results: ResultSummary) extends Concept with MultiRow {

  def columns = {
    case "pass" => Expect(results.totalTally.pass)
    case "fail" => Expect(results.totalTally.fail)
    case "setup" => Expect(results.totalTally.setup)
    case "exception" => Expect(results.totalTally.exception)
  }

  case class Expect(actualCount: Int)(expectedCount: String) extends Digger { def actual = String.valueOf(actualCount) }
  case class Time(time: String) extends Digger { def actual = "XXX" }
}