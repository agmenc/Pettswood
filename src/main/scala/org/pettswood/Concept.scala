package org.pettswood

trait Concept {
  def row() {}
  def cell(text: String): Result
  def nestedConcepts() = Map.empty[String, () => Concept]
}

object NoConceptDefined extends Concept {
  def cell(text: String) = Exception(new RuntimeException("No Concept has been defined for " + text))
}

class Grouper(domain: DomainBridge)