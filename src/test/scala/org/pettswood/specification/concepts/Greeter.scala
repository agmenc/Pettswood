package org.pettswood.specification.concepts

import org.pettswood._

class Greeter extends Concept {
  define {
    case _ => Hello
  }

  def cell(text: String) {}

  case class Hello(expectation: String) extends Digger {
    val result = "World"
  }
}
