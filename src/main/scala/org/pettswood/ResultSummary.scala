package org.pettswood

case class ResultSummary(var results: List[Result]) {
  var pass, fail, setup, exception = 0

  tally(results)

  def tally(someResults: List[Result]) {
    someResults foreach {
      case x: Pass => pass += 1
      case x: Fail => fail += 1
      case x: Setup => setup += 1
      case x: Exception => exception += 1
    }
  }

  def accumulate(children: List[ResultSummary]): ResultSummary = {
    children foreach (child => tally(child.results))
    this
  }

  def overallPass = pass > 0 && (fail + exception) == 0

  override def toString = "Pass: " + pass + " Fail: " + fail + " Setup: " + setup + " Exception: " + exception
  override def equals(that: Any) = toString.equals(that + "")
}