package org.pettswood

trait Concept {
  def row() {}
  def cell(text: String): Result
}

object NoConcept extends Concept {
  def cell(text: String) = Exception("The NoConcept Concept supports no events")
}

class Grouper(domain: DomainBridge)