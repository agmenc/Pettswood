package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._
import specification.concepts.{MirrorFixture, Maths}
import stubs._
import org.pettswood.stubs.Results._

class DomainBridgeSpec extends Specification with Mockito {
  val config = new PettswoodConfig()
  val mixinPackages = config.mixinPackages

  "A DomainBridge" should {
    "already know about the mixin concept" in {
      new DomainBridge(mixinPackages).conceptFor("MiXiNs") must beAnInstanceOf[Mixins]
    }

    "know no other concepts until one is learnt" in {
      val domain = new DomainBridge(mixinPackages)

      domain.conceptFor("mixins") must haveClass[Mixins]
      domain.currentConcept must be equalTo NoConceptDefined
    }

    "associate Concepts with tables, using the concept name in the first cell" in {
      val domain = new DomainBridge(mixinPackages)
      domain.learn("I'm a concept", new ExpectedConcept())
      domain.currentConcept must be equalTo NoConceptDefined

      domain.table("I'm a concept")

      domain.currentConcept must beAnInstanceOf[ExpectedConcept]
    }

    "return an exception result with a useful message when the concept is unrecognised" in {
      val domain = new DomainBridge(mixinPackages)

      domain.table("Donkeys") must be equalTo Exception(new RuntimeException("Unknown concept: \"Donkeys\". Known concepts: [mixins, ignore]"))
    }

    "delegate further cell handling to the current concept" in {
      val domain = new DomainBridge(mixinPackages)
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept

      domain.cell("I'm a value")

      there was one(expectedConcept).anyCell("I'm a value")
    }

    "delegate further row notification to the current concept" in {
      val domain = new DomainBridge(mixinPackages)
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept

      domain.row()

      there was one(expectedConcept).row()
    }

    "pass failure results up to the parser" in {
      val domain = new DomainBridge(mixinPackages)
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept
      expectedConcept.anyCell(any[String]) returns Fail("Elephants")

      domain.cell("Monkeys") must be equalTo Fail("Elephants")
    }

    "catch exceptions and wrap them in an Exception Result" in {
      val domain = new DomainBridge(mixinPackages)
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept
      expectedConcept.anyCell(any[String]) throws new RuntimeException("Stuff went wrong")

      domain.cell("monkeys") must be equalTo Exception(new RuntimeException("Stuff went wrong"))
    }

    "count the results and provide a summary of them" in {
      val domain = new DomainBridge(mixinPackages)
      val expectedConcept = mock[Concept]
      domain.currentConcept = expectedConcept
      expectedConcept.anyCell(any[String]) returns PASS thenReturns FAIL thenReturns FAIL thenReturns SETUP thenReturns EXCEPTION

      domain.cell("1")
      domain.cell("2")
      domain.cell("3")
      domain.cell("4")
      domain.cell("5")

      domain.summary must be equalTo ResultSummary(List(PASS, FAIL, FAIL, SETUP, EXCEPTION).reverse)
    }

    "spawn nested tables that know Concepts provided by the current Concept" in {
      val domain = new DomainBridge(mixinPackages)
      domain.currentConcept = mock[Concept]
      domain.currentConcept.nestedConcepts() returns Map(("maths", () => new Maths), ("mirror", () => new MirrorFixture))

      val nestling = domain.nestedDomain()

      nestling.conceptFor("maths") must beAnInstanceOf[Maths]
      nestling.conceptFor("mirror") must beAnInstanceOf[MirrorFixture]
      nestling.conceptFor("sausage") must throwA[RuntimeException]
    }

    "gather results from its nestlings" in {
      val domain = new DomainBridge(mixinPackages)
      val son = domain.nestedDomain()
      val daughter = domain.nestedDomain()

      domain.results = List(PASS)
      son.results = List(PASS, FAIL)
      daughter.results = List(SETUP, EXCEPTION)

      domain.summary must be equalTo ResultSummary(List(PASS, PASS, FAIL, SETUP, EXCEPTION))
    }
  }
}

