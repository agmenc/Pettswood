package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.pettswood.stubs.Results._

class ResultSummarySpec extends SpecificationWithJUnit with Mockito {
  "The result summary" should {
    "be an overall fail if no Pass results were received" in {
      new ResultSummary(NONE).overallPass must be equalTo false
    }
    "be an overall pass if only Pass and Setup results were received" in {
      new ResultSummary(List(PASS, SETUP)).overallPass must be equalTo true
    }
    "be an overall fail if any Fail or Exception results were received" in {
      new ResultSummary(List(PASS, FAIL)).overallPass must be equalTo false
      new ResultSummary(List(PASS, EXCEPTION)).overallPass must be equalTo false
    }
    "provide a summary of results" in {
      val summary = new ResultSummary(List(PASS, PASS, PASS, FAIL, FAIL, SETUP, EXCEPTION, EXCEPTION))

      summary.pass must be equalTo 3
      summary.fail must be equalTo 2
      summary.setup must be equalTo 1
      summary.exception must be equalTo 2
    }
  }
}