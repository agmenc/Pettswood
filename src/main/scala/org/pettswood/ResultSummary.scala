package org.pettswood

class ResultSummary(results: List[Result]) {
  var right, wrong, setup, exception = 0
  results foreach {
    case x: Pass => right += 1
    case x: Fail => wrong += 1
    case x: Setup => setup += 1
    case x: Exception => exception += 1
  }

  def pass = right > 0 && (wrong + exception) == 0
}