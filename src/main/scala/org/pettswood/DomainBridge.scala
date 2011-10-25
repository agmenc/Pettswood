package org.pettswood

class DomainBridge {

  var concepts = Map.empty[String, () => Concept]
  var currentConcept: Concept = NoConcept
  var results = List.empty[Result]
  var tableUntouched = false;

  def table(firstCellText: String): Result = {
    try {
      // TODO - collapse with a handleWith(handler) { ... }
      currentConcept = conceptFor(firstCellText)
      tableUntouched = true;
      Setup()
    } catch {
      case e => println("e.getMessage: " + e.getMessage); Exception(firstCellText, e getMessage)
    }
  }

  def row() {currentConcept.row()}

  // TODO - first-cellness should be a Concept concern, e.g. a SingleRow concept, vs a MultiRow
  def cell(text: String): Result = {
    if (tableUntouched) touchTable
    else {
      try {
        // TODO - collapse with a handleWith(handler) { ... }
        currentConcept.cell(text)
      } catch {
        case e => println("e.getMessage: " + e.getMessage); Exception(text, e getMessage)
      }
    }
  }

  def learn(name: String, conceptoriser: () => Concept) {concepts += ((name toLowerCase) -> conceptoriser)}

  def summary: ResultSummary = new ResultSummary(results)

  def touchTable: Setup = {tableUntouched = false; Setup()}

  def conceptFor(conceptName: String): Concept = concepts.get((conceptName toLowerCase)) match {
    case Some(conceptoriser) => conceptoriser()
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Do you need to mix in some concepts?")
  }
}

object DomainBridge extends DomainBridge {
  learn("mixins", () => new Mixins(this))
}
