package org.pettswood


trait Concept {
  def row() {}
  protected def cell(text: String): Result
  def nestedConcepts() = Map.empty[String, () => Concept]

  var firstCell = true
  def anyCell(text: String): Result = if (firstCell) {firstCell = false; Setup()} else cell(text)
}

object NoConceptDefined extends Concept {
  def cell(text: String) = Exception(new RuntimeException("No Concept has been defined for " + text))
}

// TODO - CAS - 14/04/2012 - subclasses shouldn't have to know about DomainBridge. They should just call learn() on Mixin.
class Mixin(domain: DomainBridge)