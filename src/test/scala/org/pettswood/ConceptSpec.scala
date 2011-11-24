package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito

class ConceptSpec extends SpecificationWithJUnit with Mockito {
  "Concepts should always ignore the first cell" in {
    var results = List.empty[String]
    val someConcept = new Concept { protected def cell(text: String) = {results = text :: results; Pass(text)} }

    val firstResult = someConcept.anyCell("One")
    someConcept.anyCell("Two")
    someConcept.anyCell("Three")

    firstResult must be equalTo Setup()
    results must be equalTo List("Three", "Two")
  }
}