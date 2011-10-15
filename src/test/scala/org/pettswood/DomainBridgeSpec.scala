package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._
import stubs._

class DomainBridgeSpec extends Specification with Mockito {

  "The DomainBridge instance" should {
    "already know about the mixin concept" in {
      DomainBridge.conceptFor("MiXiNs") must beAnInstanceOf[Mixins]
    }
  }
  
  "A DomainBridge" should {
    "know no concepts until one is learnt" in {
      val domain = new DomainBridge()

      domain.concepts must be equalTo Map.empty[String, Concept]
      domain.currentConcept must be equalTo NoConcept
    }
    "associate Concepts with tables, using the concept name in the first cell" in {
      val domain = new DomainBridge()
      domain.learn("I'm a concept", new ExpectedConcept())
      domain.currentConcept must be equalTo NoConcept

      domain.cell("I'm a concept")

      domain.currentConcept must beAnInstanceOf[ExpectedConcept]
    }
    "return a failure result with a useful message when the concept is unrecognised" in {
      val domain = new DomainBridge()
      
      domain.cell("Donkeys") must throwA[RuntimeException] (message = "Unknown concept: \"Donkeys\". Do you need to mix in some concepts?")
    }
    "delegate further cell handling to the current concept" in {
      val domain = new DomainBridge()
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept

      domain.cell("I'm a value")

      there was one (expectedConcept).cell("I'm a value")
    }
    "catch exceptions and wrap them in an Exception Result" in {
      failure
    }
  }

  "cell handler" should {
    "return a response that encapsulates the test behaviour" in {
      DomainBridge.cell("Monkeys") must be equalTo Fail("Elephants", "Monkeys")
    }
  }
}

