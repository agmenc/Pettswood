package org.pettswood.specification.concepts

import org.pettswood._

class Pettswood extends Concept {
  columns {
    case "Test File" => FileReader
    case "Output File" => FileExists
    // case "Results" => Results(value)
  }

  def cell(text: String) = Exception("No probe defined for this column/cell", "No probe defined for this column/cell")

  case class FileReader(filePath: String) extends Doer
  case class FileExists(filePath: String) extends Digger {
    val result = "Bananas"
  }
}
