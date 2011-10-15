package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._
import stubs._

class MixinsSpec extends Specification with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
    val mixin = new Mixins(domain)
  }

  "Mixins" should {
    "teach the domain about individual concepts" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.ExpectedConcept")

      there was one(fixture.domain).learn("org.pettswood.stubs.ExpectedConcept", new ExpectedConcept)
    }
    "allow multiple concepts in multiple cells" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.ExpectedConcept")
      fixture.mixin.cell("org.pettswood.stubs.AnotherExpectedConcept")

      there was one(fixture.domain).learn("org.pettswood.stubs.ExpectedConcept", new ExpectedConcept)
      there was one(fixture.domain).learn("org.pettswood.stubs.AnotherExpectedConcept", new AnotherExpectedConcept)
    }
    "allow addition of multiple concepts using Concept groups" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.SomeGrouper")

      there was one(fixture.domain).learn("ExpectedConcept", new ExpectedConcept)
      there was one(fixture.domain).learn("AnotherExpectedConcept", new AnotherExpectedConcept)
    }
    "allow use of multiple Mixins anywhere in the test" in {
      val domain = mock[DomainBridge]

      new Mixins(domain).cell("org.pettswood.stubs.SomeGrouper")
      new Mixins(domain).cell("org.pettswood.stubs.YetAnotherExpectedConcept")

      there was one(domain).learn("ExpectedConcept", new ExpectedConcept)
      there was one(domain).learn("AnotherExpectedConcept", new AnotherExpectedConcept)
      there was one(domain).learn("org.pettswood.stubs.YetAnotherExpectedConcept", new YetAnotherExpectedConcept)
    }
  }
}
