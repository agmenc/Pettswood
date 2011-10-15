package org.pettswood

class DomainBridge {

  var concepts = Map.empty[String, Concept]
  var currentConcept: Concept = NoConcept
  var results = List.empty[Result]

  def table() {currentConcept = NoConcept}

  def cell(text: String): Result = {
    // All of these cases should resolve to a Result (including the Exception cases)
    try {
      currentConcept match {
        case NoConcept => currentConcept = conceptFor(text); Setup()
        case concept => concept.cell(text)
      }
    } catch {
      case runtime: RuntimeException => println("runtime: " + runtime); Exception(text, runtime getMessage)
      case throwable: Throwable => println("throwable: " + throwable); Exception(text, throwable getMessage)
    }
  }

  def learn(name: String, concept: Concept) {concepts += ((name toLowerCase) -> concept)}

  def summary: ResultSummary = new ResultSummary(results)

  def conceptFor(conceptName: String): Concept = concepts.get((conceptName toLowerCase)) match {
    case Some(concept) => concept
    case None => throw new RuntimeException("Unknown concept: \"" + conceptName + "\". Do you need to mix in some concepts?")
  }
}

object DomainBridge extends DomainBridge {
  learn("mixins", new Mixins(this))
}
