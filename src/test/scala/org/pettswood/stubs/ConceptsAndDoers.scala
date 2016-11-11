package org.pettswood.stubs

import org.pettswood._

class ConceptsAndDoers // Stop the IDE from whinging

class StubbedConcept[T] extends Concept {
  def cell(text: String) = Pass(text)
  override def equals(p1: Any) = true
}

class SomeMixin(domain: DomainBridge) extends Mixin(domain) {
  domain.learn("ExpectedConcept", () => new ExpectedConcept( /* dependencies */ ))
  domain.learn("AnotherExpectedConcept", () => new AnotherExpectedConcept( /* dependencies */ ))
}

class ExpectedConcept extends StubbedConcept[ExpectedConcept]
class AnotherExpectedConcept extends StubbedConcept[AnotherExpectedConcept]
class YetAnotherExpectedConcept extends StubbedConcept[YetAnotherExpectedConcept]
case class SomeDoer(someData: String) extends Doer
