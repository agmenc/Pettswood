package org.pettswood.specification.concepts

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.pettswood.{Concept, Pass}

class MathsSpec extends SpecificationWithJUnit with Mockito {

  // TODO - push into a MultiRowTesting trait
  def givenColumns(concept: Concept, headings: String*) {
    concept.row()
    givenData(concept, headings: _*)
  }

  // TODO - push into a MultiRowTesting trait
  def givenData(concept: Concept, data: String*) {
    concept.row()
    data foreach {element => concept.cell(element)}
  }

  "When we do maths, we" should {
    "Associate the second row of column headers with Probes from our probe library" in {
      val maths = new Maths()

      givenColumns(maths, "a", "=")

      maths.probeTemplate must be equalTo List(maths.CalculationPerformer, maths.CalculationAssembler)
    }
    "Use the probes to interpret subsequent rows of cells" in {
      val maths = new Maths()

      givenColumns(maths, "a", "f", "b")
      givenData(maths, "7", "-", "3")

      maths.keyPresses must be equalTo List("3", "-", "7")
    }
    "use the operands and operator we accumulate to calculate the result" in {
      val maths = new Maths()

      givenColumns(maths, "a", "f", "b", "=")
      givenData(maths, "7", "-", "3")

      maths.cell("4") must be equalTo Pass("4")
    }
  }
}