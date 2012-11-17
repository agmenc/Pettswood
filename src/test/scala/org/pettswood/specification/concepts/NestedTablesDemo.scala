package org.pettswood.specification.concepts

import org.pettswood._

class NestedTablesDemo extends Concept with MultiRow {
  var number = "0"

  def columns = {
    case "Addition" | "Mirroring" | "More Nesting" => DoNothingProbe
    case "Read a number" => (text: String) => new Doer {number = text}
    case "Expect double" => (text: String) => new Digger {
      def actual = (2 * number.toInt).toString
    }
  }

  override def nestedConcepts() = Map(
    "Nested Tables Demo" -> (() => new NestedTablesDemo),
    "Mirror" -> (() => new MirrorFixture),
    "Maths" -> (() => new Maths)
  )
}