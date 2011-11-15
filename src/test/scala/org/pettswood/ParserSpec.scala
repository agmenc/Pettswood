package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
class ParserSpec extends SpecificationWithJUnit with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
    domain.cell(any[String]) returns Pass("Monkeys")
  }

  "when html does not contains tables, the parser" should {
    "not bother the domain" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(
        <html>
          <tag>value</tag>
          <list>
            <monkey>1</monkey>
            <monkey>2</monkey>
          </list>
        </html>
      )

      there was no(fixture.domain).table(any[String])
      there was no(fixture.domain).row()
      there was no(fixture.domain).cell(any[String])
    }
  }

  "when html contains tables, the parser" should {
    "delegate table, row and cell handling to the domain" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(
        <table>
          <tr>
            <td>Hello</td>
            <td>World</td>
          </tr>
        </table>
      )

      there was one(fixture.domain).table("Hello")
      there was one(fixture.domain).row()
      there was one(fixture.domain).cell("World")
    }
    "jang pass/fail classes into the output" in {
      val fixture = new Fixture()
      fixture.domain.cell("pass") returns Pass("pass")
      fixture.domain.cell("fail") returns Fail("monkeys")
      fixture.domain.cell("setup") returns Setup()
      fixture.domain.cell("exception") returns Exception(new RuntimeException("the computer has gone to lunch"))

      (new Parser(fixture.domain).parse(<td>pass</td>) \ "@class" text) must be equalTo "Pass"
      (new Parser(fixture.domain).parse(<td>fail</td>) \ "@class" text) must be equalTo "Fail"
      (new Parser(fixture.domain).parse(<td>setup</td>) \ "@class" text) must be equalTo "Setup"
      (new Parser(fixture.domain).parse(<td>exception</td>) \ "@class" text) must be equalTo "Exception"
    }
    "display expected vs actual for failure results" in {
      val fixture = new Fixture()
      fixture.domain.cell("sausage") returns Fail("potato")

      new Parser(fixture.domain).parse(<td>sausage</td>) must be equalTo
        <td class="Fail"><span class="result">potato<br></br>but expected:<br></br></span>sausage</td>
    }
    "display exception results" in {
      val fixture = new Fixture()
      fixture.domain.cell("sausage") returns Exception(new NullPointerException("Your pointy things are all null"))

      // TODO - children of result are NodeSeq and children of expect are ArrayBuffer. WTF?!?
      val result = new Parser(fixture.domain).parse(<td>sausage</td>)
      val expect = <td class="Exception"><span class="result">java.lang.NullPointerException: Your pointy things are all null<br></br>Expected:<br></br></span>sausage</td>
      result.toString() must be equalTo expect.toString()
    }
    "respect existing classes" in {
      val fixture = new Fixture()
      fixture.domain.cell("expected") returns Pass("actual")

      new Parser(fixture.domain).parse(<td class="displayElegantly">expected</td>) must be equalTo <td class="displayElegantly Pass">expected</td>
    }
    "recurse into nested tables, wrapping them in a div" in {
      val fixture = new Fixture()
      val nestlingDomain = mock[DomainBridge]
      nestlingDomain.cell(any[String]) returns Pass("Monkeys")
      fixture.domain.nestedDomain() returns nestlingDomain

      new Parser(fixture.domain).parse(<td><table><td>Nested Table</td></table></td>) must be equalTo <td><div><table><td class="Pass">Nested Table</td></table></div></td>
      
      there was one(nestlingDomain).table("Nested Table")
    }
  }
}