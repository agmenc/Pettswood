package org.pettswood.stubs

import org.pettswood._

class ConceptsAndDoers // Stop the IDE from whinging

class StubbedConcept[T] extends Concept {
  define { case _ => SomeDoer }
  def cell(text: String) = Pass(text)
  override def equals(p1: Any) = p1.isInstanceOf[T]
}

class SomeGrouper(domain: DomainBridge) extends Grouper(domain) {
  domain.learn("ExpectedConcept", new ExpectedConcept( /* dependencies */ ))
  domain.learn("AnotherExpectedConcept", new AnotherExpectedConcept( /* dependencies */ ))
}

class ExpectedConcept extends StubbedConcept[ExpectedConcept]
class AnotherExpectedConcept extends StubbedConcept[AnotherExpectedConcept]
class YetAnotherExpectedConcept extends StubbedConcept[YetAnotherExpectedConcept]
case class SomeDoer(someData: String) extends Doer
