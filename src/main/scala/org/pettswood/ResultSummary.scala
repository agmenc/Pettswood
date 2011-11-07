package org.pettswood

case class ResultSummary(results: List[Result]) {
  var pass, fail, setup, exception = 0
  results foreach {
    case x: Pass => pass += 1
    case x: Fail => fail += 1
    case x: Setup => setup += 1
    case x: Exception => exception += 1
  }

  def overallPass = pass > 0 && (fail + exception) == 0

  override def toString = "Pass: " + pass + " Fail: " + fail + " Setup: " + setup + " Exception: " + exception
}