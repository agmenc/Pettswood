package org.pettswood.specification.concepts

import org.pettswood._

class Maths extends Concept with MultiRow {

  val calculator = new Calculator
  var keyPresses = List.empty[String]

  override def initialiseRow() {keyPresses = List.empty[String]}

  def columns = {
    case "a" | "f" | "b" => CalculationAssembler
    case "=" => CalculationPerformer
  }

  case class CalculationAssembler(text: String) extends Doer {keyPresses = text :: keyPresses}
  case class CalculationPerformer(text: String) extends Digger {def actual = calculator.calculate(keyPresses)}
}