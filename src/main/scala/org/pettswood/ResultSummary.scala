package org.pettswood

case class ResultSummary(results: List[Result], children: List[ResultSummary]) {

  val totalTally: Tally = (tally(results) /: children)((aggregator, nextChild) => aggregator.plus(nextChild.totalTally))

  def tally(someResults: List[Result]): Tally =  {
    var pass, fail, setup, exception = 0
    someResults foreach {
      case x: Pass => pass += 1
      case x: Fail => fail += 1
      case x: Setup => setup += 1
      case x: Exception => exception += 1
    }
    Tally(pass, fail, setup, exception)
  }

  def overallPass = totalTally.overallPass
  override def toString = totalTally.toString
  override def equals(that: Any) = totalTally.toString.equals(that + "")
}

object ResultSummary {
  def apply(results: List[Result]): ResultSummary = ResultSummary(results, List.empty[ResultSummary])
}

case class Tally(pass: Int, fail: Int, setup: Int, exception: Int) {
  override def toString = "Pass: " + pass + " Fail: " + fail + " Setup: " + setup + " Exception: " + exception
  def overallPass = pass > 0 && (fail + exception) == 0
  def plus(that: Tally): Tally = Tally(pass + that.pass, fail + that.fail, setup + that.setup, exception + that.exception)
}