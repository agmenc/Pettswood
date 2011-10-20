package org.pettswood.specification.concepts

import org.pettswood._

class Mirror extends Concept {
  var state = List.empty[String]

  def cell(text: String) = {
    state = text :: state
    state match {
      case List(input) => Pass(input)
      case List("becomes", _) => Setup()
      case List(expected, _, input) => Result.resultFor(expected, reverse(input))
      case _ => throw new RuntimeException("A impossible thing has just occurred: " + state)
    }
  }

  def reverse(input: String): String = ("" /: input.toList.reverse)(_ + _)
}