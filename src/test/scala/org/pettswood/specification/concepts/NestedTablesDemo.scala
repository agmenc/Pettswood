package org.pettswood.specification.concepts

import org.pettswood._

class NestedTablesDemo extends Concept with MultiRow {
  var number = "0"

  def columns = {
    case "Addition" | "Mirroring" | "More Nesting" => DoNothingProbe
    case "Read a number" => doThis(number = _)
    case "Expect double" => dig{(text) => (2 * number.toInt).toString}
  }

  override def nestedConcepts() = Map(
    "Nested Tables Demo" -> (() => new NestedTablesDemo),
    "Mirror" -> (() => new MirrorFixture),
    "Maths" -> (() => new Maths)
  )
}