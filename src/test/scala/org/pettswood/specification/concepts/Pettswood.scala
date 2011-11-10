package org.pettswood.specification.concepts

import org.pettswood._
import java.io.File

class Pettswood extends Concept with MultiRow {

  val domain = new DomainBridge

  def probeLibrary = {
    case "Test File" => PettswoodRunner
    case "Output File" => FileExists
    case "Results" => Nestlings
  }

  override def row() { super.row(); println("currentProbes: " + currentProbes) }

  case class PettswoodRunner(filePath: String) extends Doer {
    new org.pettswood.Runner(new Parser(domain), new FileSystem).run(filePath)
  }
  case class FileExists(filePath: String) extends Digger {
    val result = if (!new File(filePath).exists()) "File not found" else filePath
  }

  case class Nestlings(nestedTable: String) extends Doer
}
