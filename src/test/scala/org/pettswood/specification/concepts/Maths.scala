package org.pettswood.specification.concepts

import org.pettswood._
import org.pettswood.Result._

// TODO - lots of this belongs in MultiRowConcept
class Maths extends Concept {

  // Generic stuff -------------------------------------------

  var rowPointer = 0;
  var currentProbes, probeTemplate = List.empty[(String) => Probe]

  override def row() {
    rowPointer += 1
    currentProbes = probeTemplate.map(x => x).reverse
    clearRow()
  }

  def cell(text: String) = rowPointer match {
    case 1 => Pass(text)
    case 2 => probeTemplate = probeLibrary(text) :: probeTemplate; Setup()
    case x => probe(text)
  }

  def probe(text: String): Result = {
    val probe = currentProbes.head(text)
    currentProbes = currentProbes.tail
    probe match {
      case doer: Doer => Setup()
      case digger: Digger => resultFor(digger.result, text)
    }
  }

  // End of generic stuff -------------------------------------------

  // Do we need the Probe/Doer/Digger concept? Can we just expect partial functions instead?

  var bits = List.empty[String]
  def clearRow() { bits = List.empty[String] }

  probeLibrary = {
    case "a" | "f" | "b" => CalculationAssembler
    case "=" => CalculationPerformer
  }

  case class CalculationAssembler(text: String) extends Doer {
    bits = text :: bits
  }

  case class CalculationPerformer(text: String) extends Digger {
    def result = {
      val x = bits(1) match {
        case "+" => (bits(2).toInt + bits(0).toInt) + ""
        case "-" => (bits(2).toInt - bits(0).toInt) + ""
        case _ => throw new RuntimeException("Unsupported mathematical operation: " + bits(1))
      }
      println("bits: " + bits + " = " + x)
      x
    }
  }
}