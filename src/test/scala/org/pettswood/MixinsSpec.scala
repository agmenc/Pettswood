package org.pettswood

import org.mockito.ArgumentMatchers.same
import org.pettswood.stubs.{ExpectedConcept, _}
import org.specs2.mock._
import org.specs2.mutable._

class MixinsSpec extends SpecificationWithJUnit with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
    val mixinPackages = new PettswoodConfig(mixinPackages = Seq("")).mixinPackages
    val mixin = new Mixins(domain, mixinPackages)
  }

  "Mixins" should {
    "teach the domain about individual concepts" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.ExpectedConcept")

      there was one(fixture.domain).learn(same("org.pettswood.stubs.ExpectedConcept"), any[() => ExpectedConcept])
    }

    "allow multiple concepts in multiple cells" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.ExpectedConcept")
      fixture.mixin.cell("org.pettswood.stubs.AnotherExpectedConcept")

      there was one(fixture.domain).learn(same("org.pettswood.stubs.ExpectedConcept"), any[() => ExpectedConcept])
      there was one(fixture.domain).learn(same("org.pettswood.stubs.AnotherExpectedConcept"), any[() => AnotherExpectedConcept])
    }

    "allow addition of multiple concepts" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.SomeMixin")

      there was one(fixture.domain).learn(same("ExpectedConcept"), any[() => ExpectedConcept])
      there was one(fixture.domain).learn(same("AnotherExpectedConcept"), any[() => AnotherExpectedConcept])
    }

    "allow use of multiple Mixins anywhere in the test" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.stubs.SomeMixin")
      fixture.mixin.cell("org.pettswood.stubs.YetAnotherExpectedConcept")

      there was one(fixture.domain).learn(same("ExpectedConcept"), any[() => ExpectedConcept])
      there was one(fixture.domain).learn(same("AnotherExpectedConcept"), any[() => AnotherExpectedConcept])
      there was one(fixture.domain).learn(same("org.pettswood.stubs.YetAnotherExpectedConcept"), any[() => YetAnotherExpectedConcept])
    }

    "Supports concepts defined in PettswoodConfig" in {
      val domain = mock[DomainBridge]
      val mixin = new Mixins(domain, Seq("org.pettswood.stubs"))

      mixin.cell("ExpectedConcept")

      there was one(domain).learn(same("ExpectedConcept"), any[() => ExpectedConcept])
    }

    "Throws underlying exception when Mixins fail to be instantiated properly" in {
      val fixture = new Fixture()

      fixture.mixin.cell("org.pettswood.BadMixin") must throwAn[IllegalArgumentException]
    }
  }
}

class BadMixin(domainBridge: DomainBridge) extends Mixin(domainBridge) {
  throw new IllegalArgumentException("Religion")
}