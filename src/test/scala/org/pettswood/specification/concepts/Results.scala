package org.pettswood.specification.concepts

import org.pettswood._

class Results(results: ResultSummary) extends Concept with MultiRow {

  def probeLibrary = {
    case "pass" => Expect(results.totalTally.pass)
    case "fail" => Expect(results.totalTally.fail)
    case "setup" => Expect(results.totalTally.setup)
    case "exception" => Expect(results.totalTally.exception)
    case "time" => Expect(results.totalTally.exception)
  }

  case class Expect(count: Int)(text: String) extends Digger { def result = String.valueOf(count) }
  case class Time(time: String) extends Digger { def result = "XXX" }
}