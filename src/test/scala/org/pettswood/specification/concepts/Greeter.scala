package org.pettswood.specification.concepts

import org.pettswood._

class Greeter extends Concept {
  var message = List.empty[String]

  def cell(text: String) = {
    message = text :: message
    println("message: " + message)
    message match {
      case List("Hello") => Pass(text)
      case List(first) => Fail("Hello", text)
      case List("World", first) => Pass(text)
      case List(second, first) => Fail("World", text)
      case _ => Exception("Not sure how you got to here", "It all went horribly wrong")
    }
  }
}