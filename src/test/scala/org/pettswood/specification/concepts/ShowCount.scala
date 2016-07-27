package org.pettswood.specification.concepts

import org.pettswood._

// Provides a default stateful class instance, in case one doesn't exist
class ShowCount(state: State) extends Concept {
  lazy val counter: StatefulCounter = state.retrieve(() => new StatefulCounter)

  def cell(text: String) = Result.given(text, counter.count.toString)
}

// Assumes state already exists, but handles the error when it hasn't
class Increment(state: State) extends Concept {
  lazy val counter: Option[StatefulCounter] = state.retrieve[StatefulCounter]()

  def cell(text: String) = text match {
    case "times" => Setup()
    case number => incrementCounter(number)
  }

  def incrementCounter(number: String): Result =
    counter match {
      case Some(c) => c.increment(number.toInt); Setup()
      case None => Fail("No counter has been created for this test")
    }
}

class StatefulCounter {
  var count = 0

  def increment(by: Int) {
    count += by
  }
}
