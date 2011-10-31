package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import org.specs2.execute.Failure

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
      fixture.domain.cell("exception") returns Exception("the computer has gone to lunch")

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
    "add a roll-up for exception results" in {
      val fixture = new Fixture()
      fixture.domain.cell("expected") returns Fail("actual")

      new Parser(fixture.domain).parse(<td>expected</td>) must be equalTo
        <td>
          <span class="calloutLink">expected</span> <div class="callout" title="Sausage">actual</div>
        </td>
    }
    "respect existing classes" in {
      val fixture = new Fixture()
      fixture.domain.cell("expected") returns Pass("actual")

      new Parser(fixture.domain).parse(<td class="displayElegantly">expected</td>) must be equalTo <td class="displayElegantly Pass">expected</td>
    }
  }

  val HTML_NESTED_TABLE =
    <html>
      <table>
        <tr>
          <td>Mixins</td>
          <td>org.pettswood.specification.groups.PettswoodBootstrap</td>
        </tr>
      </table>
      <table>
        <tr>
          <td>Hello</td>
        </tr>
        <tr>
          <td>
            <table>
              <tr>
                <td>Bonjour</td>
              </tr>
              <tr>
                <td>Sausage</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </html>

  "When handling HTML with nested tables" should {
    "also delegate table handling to the domain" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(HTML_NESTED_TABLE)

      there was one(fixture.domain).table("Mixins")
      there was one(fixture.domain).table("Hello")
      there was one(fixture.domain).table("Bonjour")
      there were 6.times(fixture.domain).cell(any[String])
    }
    "detect the nested table when it parses the cell and stack the parent table" in {
      Failure("Test failed due to lack of testyness. Infinite monkeys required")
    }
  }
}