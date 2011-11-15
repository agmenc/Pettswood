package org.pettswood.specification.concepts

import org.pettswood._
import java.io.File

class Pettswood extends Concept with MultiRow {

  var nestedDomain: DomainBridge = null

  def probeLibrary = {
    case "Test File" => PettswoodRunner
    case "Output File" => FileExists
    case "Results" => Nestlings
  }

  override def nestedConcepts() = Map("Results" -> (() => new Results(nestedDomain.summary)))

  override def row() {
    super.row();
    nestedDomain = new DomainBridge {
      override def registerResult(result: Result) = {
        println("registering result: " + result);
        super.registerResult(result)
      }
    }
  }

  case class PettswoodRunner(filePath: String) extends Doer {
    new org.pettswood.Runner(new Parser(nestedDomain), new FileSystem).run(filePath)
  }
  case class FileExists(filePath: String) extends Digger {
    val result = if (!new File(filePath).exists()) "File not found" else filePath
  }

  // TODO - This should build the nestedConcepts for this row
  case class Nestlings(nestedTable: String) extends Doer
}