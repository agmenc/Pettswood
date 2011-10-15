package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
class ResultSummarySpec extends SpecificationWithJUnit with Mockito {

  val NONE = List.empty[Result]
  val RIGHT = Pass("Well done")
  val SETUP = Setup()
  val WRONG = Fail("Well done", "Oh dear")
  val EXCEPTION = Exception("Well done", "YouHaveFailedException")

  "The result summary" should {
    "be an overall fail if no Pass results were received" in {
      new ResultSummary(NONE).pass must be equalTo false
    }
    "be an overall pass if only Pass and Setup results were received" in {
      new ResultSummary(List(RIGHT, SETUP)).pass must be equalTo true
    }
    "be an overall fail if any Fail or Exception results were received" in {
      new ResultSummary(List(RIGHT, WRONG)).pass must be equalTo false
      new ResultSummary(List(RIGHT, EXCEPTION)).pass must be equalTo false
    }
    "provide a summary of results" in {
      val summary = new ResultSummary(List(RIGHT, RIGHT, RIGHT, WRONG, WRONG, SETUP, EXCEPTION, EXCEPTION))

      summary.right must be equalTo 3
      summary.wrong must be equalTo 2
      summary.setup must be equalTo 1
      summary.exception must be equalTo 2
    }
  }
}