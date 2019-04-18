package org.pettswood

class DomainBridge(mixinPackages: Seq[String]) {
  var concepts = Map.empty[String, () => Concept]
  val state = new State
  var currentConcept: Concept = NoConceptDefined
  var results: List[Result] = Nil
  var nestlings = List.empty[DomainBridge]

  learn("mixins", () => new Mixins(this, mixinPackages))
  learn("ignore", () => Ignore)

  def table(captionText: String): Result = tryThis { currentConcept = conceptFor(captionText); Uninteresting() }
  def ignoreTable(): Result = tryThis { currentConcept = Ignore; Uninteresting() }
  def header(header: String): Result = tryThis { currentConcept.initHeader(header) }
  def newRow() :Unit = { currentConcept.initRow() }
  def rowEnd(): Unit = { currentConcept.endRow() }
  def cell(text: String): Result =  tryThis { registerResult(currentConcept.cell(text)) }

  private def tryThis(f: => Result): Result = try {f} catch { case e: Throwable => registerResult(Exception(e)) }

  def registerResult(result: Result): Result = {
    results = result :: results
    result
  }

  def nestedDomain(): DomainBridge = {
    val nestling = new DomainBridge(mixinPackages)
    nestlings = nestling :: nestlings
    currentConcept.nestedConcepts().foreach { case (k, v) => nestling.learn(k, () => v()) }
    nestling
  }

  // TODO - make learn() accept a varargs of (name, conceptoriser): _*
  def learn(name: String, conceptoriser: () => Concept): DomainBridge = {concepts += ((name toLowerCase) -> conceptoriser); this}
  def summary: ResultSummary = ResultSummary(results, nestlings.map(_.summary))

  def conceptFor(conceptName: String): Concept = concepts.get(conceptName toLowerCase) match {
    case Some(conceptoriser) => conceptoriser()
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Known concepts: [" + concepts.keys.mkString(", ") + "]")
  }
}