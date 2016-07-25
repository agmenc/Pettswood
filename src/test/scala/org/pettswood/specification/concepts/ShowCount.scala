package org.pettswood.specification.concepts

import org.pettswood._

class ShowCount(state: State) extends Concept {
  val counter = state.retrieve(() => new StatefulCounter)

  def cell(text: String) = Result.given(text, counter.count.toString)
}

class Increment(state: State) extends Concept {
  val counter = state.retrieve(() => new StatefulCounter)

  def cell(text: String) = text match {
    case "times" => Setup()
    case number => counter.increment(number.toInt); Setup()
  }
}

class StatefulCounter {
  var count = 0

  def increment(by: Int) {
    count += by
  }
}
