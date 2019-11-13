package org.pettswood


trait Concept {

  def initRow() {}
  def endRow() {}
  def endTable() {}
  def initHeader(header: String) : Result = Uninteresting()
  def cell(text: String): Result
  def nestedConcepts() = Map.empty[String, () => Concept]
}

object NoConceptDefined extends Concept {
  def cell(text: String) = Exception(new RuntimeException("No Concept has been defined for " + text))
}

// TODO: Ignoration of whole tests
object Ignore extends Concept {
  def cell(text: String) = Setup()
}

// TODO - CAS - 14/04/2012 - subclasses shouldn't have to know about DomainBridge. They should just call learn() on Mixin.
class Mixin(domain: DomainBridge)