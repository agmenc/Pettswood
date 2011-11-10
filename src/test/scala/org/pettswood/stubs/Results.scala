package org.pettswood.stubs

import org.pettswood._

object Results {
  val NONE = List.empty[Result]
  val PASS = Pass("Well done")
  val SETUP = Setup()
  val FAIL = Fail("Oh dear")
  val EXCEPTION = Exception(new RuntimeException("A known unknown just happened to you"))
}