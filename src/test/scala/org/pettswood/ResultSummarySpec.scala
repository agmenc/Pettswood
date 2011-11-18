package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.pettswood.stubs.Results._

class ResultSummarySpec extends SpecificationWithJUnit with Mockito {
  "The result summary" should {
    "be an overall fail if no Pass results were received" in {
      ResultSummary(NONE).overallPass must be equalTo false
    }
    "be an overall pass if only Pass and Setup results were received" in {
      ResultSummary(List(PASS, SETUP)).overallPass must be equalTo true
    }
    "be an overall fail if any Fail or Exception results were received" in {
      ResultSummary(List(PASS, FAIL)).overallPass must be equalTo false
      ResultSummary(List(PASS, EXCEPTION)).overallPass must be equalTo false
    }
    "provide a summary of results" in {
      val tally = ResultSummary(List(PASS, PASS, PASS, FAIL, FAIL, SETUP, EXCEPTION, EXCEPTION)).totalTally

      tally.pass must be equalTo 3
      tally.fail must be equalTo 2
      tally.setup must be equalTo 1
      tally.exception must be equalTo 2
    }
    "swallow other ResultSummary objects and accumulate their results" in {
      val grandChild1 = ResultSummary(List(PASS))
      val child1 = ResultSummary(List(PASS, FAIL), List(grandChild1))
      val grandChild2 = ResultSummary(List(SETUP))
      val child2 = ResultSummary(List(SETUP, EXCEPTION), List(grandChild2))

      val actual = ResultSummary(List.empty[Result], List(child1, child2))

      actual must be equalTo ResultSummary(List(PASS, PASS, FAIL, SETUP, SETUP, EXCEPTION))
    }
  }
}