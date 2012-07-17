package org.pettswood.specification.concepts

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.pettswood._
import org.pettswood.Pass

class MathsSpec extends SpecificationWithJUnit with Mockito {

  // TODO - push into a MultiRowTesting trait
  def givenColumns(concept: Concept, headings: String*) {
    concept.row()
    concept.anyCell("title cell")
    givenDataRow(concept, headings: _*)
  }

  // TODO - push into a ConceptTesting trait
  def givenDataRow(concept: Concept, data: String*) {
    concept.row()
    data foreach {element => concept.anyCell(element)}
  }

  // TODO - CAS - 14/07/2012 - Push into a MultiRowSpec
  val genericMultiRow = new MultiRow {
    def columns = {
      case "a" => dummyProbe
      case "b" => blowUpProbe
    }
  }

  def dummyProbe(some: String) = new Doer{}
  def blowUpProbe(some: String) = new Doer{ throw new RuntimeException("Self destruct sequence initiated") }

  "The probe for the current column is always removed from the list, even when exceptions are thrown" in {
    givenColumns(genericMultiRow, "a", "b")
    genericMultiRow.row()
    genericMultiRow.currentProbes.size must be equalTo 2

    givenDataRow(genericMultiRow, "value for column a")
    genericMultiRow.currentProbes.size must be equalTo 1

    givenDataRow(genericMultiRow, "value for column a", "value for column b") should throwA[RuntimeException]
    genericMultiRow.currentProbes.size must be equalTo 0
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
      givenDataRow(maths, "7", "-", "3")

      maths.keyPresses must be equalTo List("3", "-", "7")
    }

    "use the operands and operator we accumulate to calculate the result" in {
      val maths = new Maths()

      givenColumns(maths, "a", "f", "b", "=")
      givenDataRow(maths, "7", "-", "3")

      maths.cell("4") must be equalTo Pass("4")
    }
  }
}