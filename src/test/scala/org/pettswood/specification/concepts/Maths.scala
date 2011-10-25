package org.pettswood.specification.concepts

import org.pettswood._

class Maths extends Concept with MultiRow {

  var bits = List.empty[String]

  override def clearRow() {bits = List.empty[String]}

  def probeLibrary = {
    case "a" | "f" | "b" => CalculationAssembler
    case "=" => CalculationPerformer
  }

  case class CalculationAssembler(text: String) extends Doer {
    bits = text :: bits
  }

  case class CalculationPerformer(text: String) extends Digger {
    def result = bits(1) match {
      case "+" => (bits(2).toInt + bits(0).toInt) + ""
      case "-" => (bits(2).toInt - bits(0).toInt) + ""
      case _ => throw new RuntimeException("Unsupported mathematical operation: " + bits(1))
    }
  }
}