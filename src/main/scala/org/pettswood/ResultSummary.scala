package org.pettswood

case class ResultSummary(results: List[Result], children: List[ResultSummary]) {

  val totalTally: Tally = (tally(results) /: children)((aggregator, nextChild) => aggregator.plus(nextChild.totalTally))

  def tally(someResults: List[Result]): Tally =  {
    val grouped = someResults.groupBy(r => r.getClass)
    def count[T <:Result](c: Class[T]) = grouped.getOrElse(c, Nil).size
    Tally(count(classOf[Pass]), count(classOf[Fail]), count(classOf[Setup]), count(classOf[Exception]))
  }

  def overallPass = totalTally.overallPass
  override def toString = totalTally.toString
  override def equals(that: Any) = totalTally.toString.equals(that + "")
}

object ResultSummary {
  def apply(results: List[Result]): ResultSummary = ResultSummary(results, List.empty[ResultSummary])
}

// TODO - CAS - 22/04/2013 - Combine with ResultSummary
case class Tally(pass: Int, fail: Int, setup: Int, exception: Int) {
  override def toString = "Pass: " + pass + " Setup: " + setup + " Fail: " + fail + " Exception: " + exception
  def overallPass = pass >= 0 && (fail + exception) == 0
  def plus(that: Tally): Tally = Tally(pass + that.pass, fail + that.fail, setup + that.setup, exception + that.exception)
}