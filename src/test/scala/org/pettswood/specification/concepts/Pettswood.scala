package org.pettswood.specification.concepts

import org.pettswood._
import java.io.File
import parsers.xml.scala.Parser
import runners.DisposableRunner

class Pettswood extends Concept with MultiRow {

  var nestedDomain: DomainBridge = null

  def columns = {
    case "Test File" => PettswoodRunner
    case "Output File" => FileExists
    case "Results" => DoNothingProbe
  }

  override def nestedConcepts() = Map("Results" -> (() => new Results(nestedDomain.summary)))

  case class PettswoodRunner(filePath: String) extends Doer {
    nestedDomain = new DomainBridge
    new DisposableRunner(new Parser(nestedDomain), new FileSystem).run(filePath)
  }

  case class FileExists(filePath: String) extends Digger {
    val result = if (!new File(filePath).exists()) "File not found" else filePath
  }
}