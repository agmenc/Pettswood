package org.pettswood

class DomainBridge {

  var concepts = Map.empty[String, () => Concept]
  var currentConcept: Concept = NoConceptDefined
  var results = List.empty[Result]
  var nestlings = List.empty[DomainBridge]
  var tableUntouched = false;

  learn("mixins", () => new Mixins(this))

  def table(firstCellText: String): Result = tryWith("Failure reading table heading") { () =>
    currentConcept = conceptFor(firstCellText)
    tableUntouched = true;
    Setup()
  }

  def row() {currentConcept.row()}

  // TODO - first-cellness should be a Concept concern, e.g. a SingleRow concept, vs a MultiRow
  def cell(text: String): Result = {
    if (tableUntouched) touchTable
    else tryWith("Failure parsing cell") { () => registerResult(currentConcept.cell(text)) }
  }

  def tryWith(message: String)(f: () => Result): Result = try {f()} catch {case e => println(message + ": " + e.getMessage); registerResult(Exception(e))}

  def registerResult(result: Result): Result = {
    results = result :: results
    result
  }

  // TODO - make learn() accept a varargs of (name, conceptoriser): _*
  def nestedDomain() = {
    val nestling = new DomainBridge
    nestlings = nestling :: nestlings
    currentConcept.nestedConcepts().foreach { x => nestling.learn(x._1, x._2) }
    nestling
  }

  def learn(name: String, conceptoriser: () => Concept) =  {concepts += ((name toLowerCase) -> conceptoriser); this}
  def summary: ResultSummary = ResultSummary(results, nestlings.map(_.summary))
  def touchTable: Setup = {tableUntouched = false; Setup()}

  def conceptFor(conceptName: String): Concept = concepts.get((conceptName toLowerCase)) match {
    case Some(conceptoriser) => conceptoriser()
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Known concepts: [" + concepts.keys.mkString(", ") + "]")
  }
}