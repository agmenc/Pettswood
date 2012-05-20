package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import parsers.xml.scala.Parser

class ParserSpec extends SpecificationWithJUnit with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
    val summary = mock[ResultSummary]
    domain.summary returns summary
    domain.cell(any[String]) returns Pass("Monkeys")
    domain.table(any[String]) returns Setup()
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
      // TODO - CAS - 20/05/2012 - failures really could be displayed better, like this:
//        <td class="Fail"><span class="strikethrough">sausage</span>potato</td>
    }
    "display exception results in cells" in {
      val fixture = new Fixture()
      fixture.domain.cell("sausage") returns Exception(new NullPointerException("Your pointy things are all null"))

      // TODO - children of result are NodeSeq and children of expect are ArrayBuffer. WTF?!?
      val result = new Parser(fixture.domain).parse(<td>sausage</td>)

      val expect = <td class="Exception"><span class="result">java.lang.NullPointerException: Your pointy things are all null<br></br>Expected:<br></br></span>sausage</td>
      result.toString() must be equalTo expect.toString()
    }
    "display exception results for unknown table headers" in {
      val fixture = new Fixture()
      fixture.domain.table("sausage") returns Exception(new RuntimeException("Unknown concept: \"sausage\". Known concepts: [mixins]"))

      val result = new Parser(fixture.domain).parse(<table><tr><td>sausage</td></tr></table>)

      val expect = <table class="Exception"><tr><td><span class="result">java.lang.RuntimeException: Unknown concept: "sausage". Known concepts: [mixins]<br></br>Expected:<br></br></span></td></tr><tr><td class="Pass">sausage</td></tr></table>
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
      nestlingDomain.table(any[String]) returns Setup()
      fixture.domain.nestedDomain() returns nestlingDomain

      new Parser(fixture.domain).parse(<td><table><td>Nested Table</td></table></td>) must be equalTo <td><div><table class="Setup"><td class="Pass">Nested Table</td></table></div></td>
      
      there was one(nestlingDomain).table("Nested Table")
    }
    "decorate output files with a results summary at the end of the body" in {
      val fixture = new Fixture()
      fixture.summary.toString returns "High Score: 472"
      fixture.summary.overallPass returns false

      val actual = new Parser(fixture.domain).decorate( <body><p/></body> )

      actual must be equalTo ( <body><p/><table><tr><td class="Setup">Results:</td><td class="Fail">High Score: 472</td></tr></table></body> )
    }
    "not decorate output files that have no body" in {
      val fixture = new Fixture()

      val actual = new Parser(fixture.domain).decorate( <div><p/></div> )

      actual must be equalTo <div><p/></div>
    }
  }
}