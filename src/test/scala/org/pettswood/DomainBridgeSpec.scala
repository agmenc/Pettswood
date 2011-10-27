package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._
import stubs._
import org.pettswood.stubs.Results._
class DomainBridgeSpec extends Specification with Mockito {

  "The DomainBridge instance" should {
    "already know about the mixin concept" in {
      DomainBridge.conceptFor("MiXiNs") must beAnInstanceOf[Mixins]
    }
  }

  "A DomainBridge" should {
    "know no concepts until one is learnt" in {
      val domain = new DomainBridge()

      domain.concepts must be equalTo Map.empty[String, () => Concept]
      domain.currentConcept must be equalTo NoConcept
    }
    "associate Concepts with tables, using the concept name in the first cell" in {
      val domain = new DomainBridge()
      domain.learn("I'm a concept", () => new ExpectedConcept())
      domain.currentConcept must be equalTo NoConcept

      domain.table("I'm a concept")

      domain.currentConcept must beAnInstanceOf[ExpectedConcept]
    }
    "return an exception result with a useful message when the concept is unrecognised" in {
      val domain = new DomainBridge()

      domain.table("Donkeys") must be equalTo Exception("java.lang.RuntimeException: Unknown concept: \"Donkeys\". Known concepts: []")
    }
    "delegate further cell handling to the current concept" in {
      val domain = new DomainBridge()
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept

      domain.cell("I'm a value")

      there was one(expectedConcept).cell("I'm a value")
    }
    "delegate further row notification to the current concept" in {
      val domain = new DomainBridge()
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept

      domain.row()

      there was one(expectedConcept).row()
    }
    "pass failure results up to the parser" in {
      val domain = new DomainBridge()
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept
      expectedConcept.cell(any[String]) returns Fail("Elephants")

      domain.cell("Monkeys") must be equalTo Fail("Elephants")
    }
    "catch exceptions and wrap them in an Exception Result" in {
      val domain = new DomainBridge()
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept
      expectedConcept.cell(any[String]) throws new RuntimeException("Stuff went wrong")

      domain.cell("monkeys") must be equalTo Exception("java.lang.RuntimeException: Stuff went wrong")
    }
    "count the results and provide a summary of them" in {
      val domain = new DomainBridge()
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept
      expectedConcept.cell(any[String]) returns PASS thenReturns FAIL thenReturns FAIL thenReturns SETUP thenReturns EXCEPTION

      domain.cell("1")
      domain.cell("2")
      domain.cell("3")
      domain.cell("4")
      domain.cell("5")

      domain.summary must be equalTo new ResultSummary(List(PASS, FAIL, FAIL, SETUP, EXCEPTION) reverse)
    }
  }
}

