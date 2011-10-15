package org.pettswood

class ResultSummary(results: List[Result]) {
  var right, wrong, setup, exception = 0
  results foreach {
    case x: Right => right += 1
    case x: Wrong => wrong += 1
    case x: Setup => setup += 1
    case x: Exception => exception += 1
  }

  def pass = right > 0 && (wrong + exception) == 0
}