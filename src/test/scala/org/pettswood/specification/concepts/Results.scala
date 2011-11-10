package org.pettswood.specification.concepts

import org.pettswood._

class Results(results: ResultSummary) extends Concept with MultiRow {

  def probeLibrary = {
    case "pass" => Expect(results.pass)
    case "fail" => Expect(results.fail)
    case "setup" => Expect(results.setup)
    case "exception" => Expect(results.exception)
    case "time" => Expect(results.exception)
  }

  case class Expect(count: Int)(text: String) extends Digger { def result = String.valueOf(count) }
  case class Time(time: String) extends Digger { def result = "XXX" }
}