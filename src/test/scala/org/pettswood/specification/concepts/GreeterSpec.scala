package org.pettswood.specification.concepts

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.pettswood.{Fail, Pass}

class GreeterSpec extends SpecificationWithJUnit with Mockito {

  "The greeter" should {
    "pass for Hello World" in {
      val greeter = new Greeter

      greeter.cell("Hello") must be equalTo Pass("Hello")
      greeter.cell("World") must be equalTo Pass("World")
    }
    "fail for any other combination" in {
      var greeter = new Greeter
      greeter.cell("Hello") must be equalTo Pass("Hello")
      greeter.cell("Elephant") must be equalTo Fail("World", "Elephant")

      greeter = new Greeter
      greeter.cell("Goodbye") must be equalTo Fail("Hello", "Goodbye")
      greeter.cell("World") must be equalTo Pass("World")

      greeter = new Greeter
      greeter.cell("Hamburger") must be equalTo Fail("Hello", "Hamburger")
      greeter.cell("Brick") must be equalTo Fail("World", "Brick")
    }
  }
}