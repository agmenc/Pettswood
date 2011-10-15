package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
class ParserSpec extends SpecificationWithJUnit with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
  }

  "html without tables" should {
    "not bother the parser" in {
      val fixture = new Fixture()

      val inputXml =
        <html>
          <tag>value</tag>
          <list>
            <monkey>1</monkey>
            <monkey>2</monkey>
          </list>
        </html>

      new Parser(fixture.domain).parse(inputXml)

      there was no(fixture.domain).table()
      there was no(fixture.domain).cell(any[String])
    }
  }

  "html with tables" should {
    "delegate table handle to the domain" in {
      val fixture = new Fixture()

      val input =
        <html>
          <table>
              <tr>
                  <td>Mixins</td>
                  <td>org.pettswood.specification.groups.PettswoodBootstrap</td>
              </tr>
          </table>
          <table>
            <tr><td>Hello</td></tr>
            <tr><td>Sausage</td></tr>
          </table>
        </html>

      new Parser(fixture.domain).parse(input)

      there were two(fixture.domain).table()
      there were 4.times(fixture.domain).cell(any[String])
    }
  }
}