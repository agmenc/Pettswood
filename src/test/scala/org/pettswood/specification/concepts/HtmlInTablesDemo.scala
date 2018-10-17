package org.pettswood.specification.concepts

import org.pettswood._

class HtmlInTablesDemo extends Concept with MultiRow {

  def columns = {
    case "Given this" => DoNothing
    case "Then do that" => dig { text =>
      text.replace("This bit of data will come back wrong", "See, it has gone all wrong")
    }
  }
}
