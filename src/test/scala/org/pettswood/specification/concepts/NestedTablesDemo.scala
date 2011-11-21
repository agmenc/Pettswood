package org.pettswood.specification.concepts

import org.pettswood._

class NestedTablesDemo extends Concept with MultiRow {

  def columns = {
    case "Addition" | "Mirroring" | "More Nesting" => DoNothingProbe
  }

  override def nestedConcepts() = Map(
    "Nested Tables Demo" -> (() => new NestedTablesDemo()),
    "Mirror" -> (() => new Mirror()),
    "Maths" -> (() => new Maths())
  )
}