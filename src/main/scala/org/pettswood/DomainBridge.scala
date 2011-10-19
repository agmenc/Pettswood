package org.pettswood

class DomainBridge {

  var concepts = Map.empty[String, Concept]
  var currentConcept: Concept = NoConcept
  var results = List.empty[Result]
  var tableUntouched = false;

  def table(firstCellText: String): Result = {
    try { // TODO - collapse with a handleWith(handler) { ... }
      currentConcept = conceptFor(firstCellText)
      tableUntouched = true;
      Setup()
    } catch {
      case e => Exception(firstCellText, e getMessage)
    }
  }

  def cell(text: String): Result = if (tableUntouched) touchTable else currentConcept.cell(text)

  def learn(name: String, concept: Concept) {concepts += ((name toLowerCase) -> concept)}

  def summary: ResultSummary = new ResultSummary(results)

  def touchTable: Setup = {tableUntouched = false; Setup()}

  def conceptFor(conceptName: String): Concept = concepts.get((conceptName toLowerCase)) match {
    case Some(concept) => concept
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Do you need to mix in some concepts?")
  }
}

object DomainBridge extends DomainBridge {
  learn("mixins", new Mixins(this))
}
