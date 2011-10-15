package org.pettswood.specification.concepts

import org.pettswood._

class Pettswood extends Concept {
  define {
    case "Test File" => FileReader
    case "Output File" => FileExists
    // case "Results" => Results(value)
  }

  def cell(text: String) {}

  case class FileReader(filePath: String) extends Doer
  case class FileExists(filePath: String) extends Digger {
    val result = "Bananas"
  }
}
