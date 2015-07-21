package org.pettswood.specification.concepts

import org.pettswood._
import files.FileSystem
import java.io.File
import parsers.xml.scala.Parser
import runners.DisposableRunner

class Pettswood extends Concept with MultiRow {

  var nestedDomain: DomainBridge = null

  def columns = {
    case "Test File" => PettswoodRunner
    case "Output File" => FileExists
    case "Results" => DoNothing
  }

  override def nestedConcepts() = Map("Results" -> (() => new TimedResults(nestedDomain.summary)))

  case class PettswoodRunner(filePath: String) extends Doer {
    nestedDomain = new DomainBridge(Seq(""))
    new DisposableRunner(new Parser(nestedDomain), new FileSystem, PettswoodConfig.current).run(filePath)
  }

  case class FileExists(filePath: String) extends Digger {
    val actual = if (!new File(filePath).exists()) "File not found" else filePath
  }
}