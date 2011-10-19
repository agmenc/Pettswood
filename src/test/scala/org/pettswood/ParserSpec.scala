package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._

class ParserSpec extends SpecificationWithJUnit with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
    domain.cell(any[String]) returns Pass("Monkeys")
  }

  val HTML_WITHOUT_TABLES =
    <html>
      <tag>value</tag>
      <list>
        <monkey>1</monkey>
        <monkey>2</monkey>
      </list>
    </html>

  val HTML_SIMPLE_TABLE =
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
          <td>Sausage</td>
        </tr>
      </table>
    </html>

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

  "html without tables" should {
    "not bother the parser" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(HTML_WITHOUT_TABLES)

      there was no(fixture.domain).table(any[String])
      there was no(fixture.domain).cell(any[String])
    }
  }

  "html with tables" should {
    "delegate table handling to the domain" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(HTML_SIMPLE_TABLE)

      there was one(fixture.domain).table("Mixins")
      there was one(fixture.domain).table("Hello")
      there were 4.times(fixture.domain).cell(any[String])
    }
    "have pass/fail classes janged into it" in {
      val fixture = new Fixture()
      fixture.domain.cell("Sausage") returns Fail("World", "Sausage")

      val result = new Parser(fixture.domain).parse(HTML_SIMPLE_TABLE)

      (result \\ "td").head must be equalTo <td class="Pass">Mixins</td>
      (result \\ "td").reverse.head must be equalTo <td class="Fail">Sausage</td>
    }
  }

  "html with nested tables" should {
    "also delegate table handling to the domain" in {
      val fixture = new Fixture()

      new Parser(fixture.domain).parse(HTML_NESTED_TABLE)

      there was one(fixture.domain).table("Mixins")
      there was one(fixture.domain).table("Hello")
      there was one(fixture.domain).table("Bonjour")
      there were 6.times(fixture.domain).cell(any[String])
    }
  }
}