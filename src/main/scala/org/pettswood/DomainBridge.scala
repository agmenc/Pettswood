package org.pettswood

class DomainBridge {

  var concepts = Map.empty[String, () => Concept]
  var currentConcept: Concept = NoConcept
  var results = List.empty[Result]
  var tableUntouched = false;

  learn("mixins", () => new Mixins(this))

  def table(firstCellText: String): Result = {
    // TODO - collapse with a handleWith(handler) { ... }
    try {
      currentConcept = conceptFor(firstCellText)
      tableUntouched = true;
      Setup()
    } catch {
      case e => println("Failure reading table heading: " + e.getMessage); registerResult(Exception(e toString))
    }
  }

  def row() {currentConcept.row()}

  def registerResult(result: Result): Result = {
    results = result :: results
    result
  }

  // TODO - first-cellness should be a Concept concern, e.g. a SingleRow concept, vs a MultiRow
  def cell(text: String): Result = {
    if (tableUntouched) touchTable
    else {
      // TODO - collapse with a handleWith(handler) { ... }
      try {
        registerResult(currentConcept.cell(text))
      } catch {
        case e => println("Failure parsing cell: " + e.getMessage); registerResult(Exception(e toString))
      }
    }
  }

  def learn(name: String, conceptoriser: () => Concept) {concepts += ((name toLowerCase) -> conceptoriser)}

  def summary: ResultSummary = new ResultSummary(results)

  def touchTable: Setup = {tableUntouched = false; Setup()}

  def conceptFor(conceptName: String): Concept = concepts.get((conceptName toLowerCase)) match {
    case Some(conceptoriser) => conceptoriser()
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Known concepts: [" + concepts.keys.mkString(", ") + "]")
  }
}