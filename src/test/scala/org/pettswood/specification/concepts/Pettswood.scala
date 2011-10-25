package org.pettswood.specification.concepts

import org.pettswood._

class Pettswood extends Concept with MultiRow {
  def probeLibrary = {
    case "Test File" => FileReader
    case "Output File" => FileExists
    case "Results" => Results
  }

  case class FileReader(filePath: String) extends Doer
  case class FileExists(filePath: String) extends Digger {
    val result = "Bananas"
  }
  case class Results(nestedTable: String) extends Digger {
    val result = "Sausages"
  }
}
