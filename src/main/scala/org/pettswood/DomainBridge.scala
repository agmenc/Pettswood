package org.pettswood

class DomainBridge(mixinPackages: Seq[String]) {

  var concepts = Map.empty[String, () => Concept]
  val state = new State
  var currentConcept: Concept = NoConceptDefined
  var results: List[Result] = Nil
  var nestlings = List.empty[DomainBridge]

  learn("mixins", new Mixins(this, mixinPackages))
  learn("ignore", Ignore)

  def table(firstCellText: String): Result = tryThis { currentConcept = conceptFor(firstCellText); Uninteresting() }
  def row() { currentConcept.row() }
  def cell(text: String): Result =  tryThis { registerResult(currentConcept.anyCell(text)) }
  private def tryThis(f: => Result): Result = try {f} catch { case e: Throwable => registerResult(Exception(e)) }

  def registerResult(result: Result): Result = {
    results = result :: results
    result
  }

  def nestedDomain() = {
    val nestling = new DomainBridge(mixinPackages)
    nestlings = nestling :: nestlings
    currentConcept.nestedConcepts().foreach { x => nestling.learn(x._1, x._2()) }
    nestling
  }

  // TODO - make learn() accept a varargs of (name, conceptoriser): _*
  def learn(name: String, conceptoriser: => Concept) =  {concepts += ((name toLowerCase) -> (() => conceptoriser)); this}
  def summary: ResultSummary = ResultSummary(results, nestlings.map(_.summary))

  def conceptFor(conceptName: String): Concept = concepts.get(conceptName toLowerCase) match {
    case Some(conceptoriser) => conceptoriser()
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Known concepts: [" + concepts.keys.mkString(", ") + "]")
  }
}