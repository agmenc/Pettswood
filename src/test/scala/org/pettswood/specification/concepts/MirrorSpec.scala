package org.pettswood.specification.concepts

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.pettswood.{Setup, Fail, Pass}

class MirrorSpec extends SpecificationWithJUnit with Mockito {

  "The mirror" should {
    "pass when the final cell contains the mirror of the input cell" in {
      val mirror = new Mirror

      mirror.cell("Hello") must be equalTo Pass("Hello")
      mirror.cell("becomes") must be equalTo Setup()
      mirror.cell("olleH") must be equalTo Pass("olleH")
    }
    "fail otherwise" in {
      val mirror = new Mirror

      mirror.cell("Hello") must be equalTo Pass("Hello")
      mirror.cell("becomes") must be equalTo Setup()
      mirror.cell("Monkeys") must be equalTo Fail("Monkeys", "olleH")
    }
  }
}