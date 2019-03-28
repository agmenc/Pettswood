package org.pettswood

import org.pettswood.Xml._
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import parsers.xml.scala.Parser
import xml._

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

    "use caption for first cell text" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(
        <table>
          <caption>Peter</caption>
          <tbody>
            <tr>
              <td>World</td>
            </tr>
          </tbody>
        </table>
      )

      there was one(fixture.domain).table("Peter")
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
        <td class="Fail"><span>potato<br></br>but expected:<br></br></span>sausage</td>
    }

    "display exception stack traces in cells" in {
      val fixture = new Fixture()
      fixture.domain.cell("sausage") returns Exception(new NullPointerException("Your pointy things are all null"))

      // TODO - children of result are NodeSeq and children of expect are ArrayBuffer. WTF?!?
      val result = new Parser(fixture.domain).parse(<td>sausage</td>)

      (result \\ "span").text must contain("Show/Hide")
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

      //      new Parser(fixture.domain).parse(<td><table><td>Nested Table</td></table></td>) must be equalTo <td><div><table><td class="Pass">Nested Table</td></table></div></td>
      new Parser(fixture.domain).parse(<td><table><caption>Nested Table</caption></table></td>) must be equalTo <td><div><table><caption>Nested Table</caption></table></div></td>
      
      there was one(nestlingDomain).table("Nested Table")
    }

    "decorate output files with a results summary at the end of the body" in {
      val fixture = new Fixture()
      fixture.summary.toString returns "High Score: 472"
      fixture.summary.overallPass returns false

      val actual = new Parser(fixture.domain).decorate( <body><p/></body> )

      actual must be equalTo <body><p/><div class="container"><div class="row"><table class="table"><tr><th>Results:</th><td class="Fail">High Score: 472</td></tr></table></div></div></body>
    }

    "not decorate output files that have no body" in {
      val fixture = new Fixture()

      val actual = new Parser(fixture.domain).decorate( <div><p/></div> )

      actual must be equalTo <div><p/></div>
    }

    "Script tags retain their closing tag, so that browsers don't ignore them" in {
      val fixture = new Fixture()

      val before =
        <html>
          <head>
            <script type="text/javascript" src="javascript/jquery-3.1.1.min.js"></script>
          </head>
          <body/>
        </html>

      val after = new Parser(fixture.domain).parse(before)

      after must equalExactly(before)
    }
  }
}

class NodeSeqWrapper(nodeSeq: NodeSeq) {
  def \@(selector: String): NodeSeq = {
    val bits = selector.split("=")
    nodeSeq.filter(node => ((node \\ ("@" + bits(0))) text) contains bits(1))
  }

  def myFilter(elem: NodeSeq): Boolean = ((elem \\ "@class") text) contains "result"
}

object NodeSeqWrapper {
  implicit def ToNodeSeqWrapper(someNodeSeq: NodeSeq): NodeSeqWrapper = new NodeSeqWrapper(someNodeSeq)
}