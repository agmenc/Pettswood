package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._

class ParserEndToEndSpec extends SpecificationWithJUnit with Mockito {

  "html without tables" should {
    "pass through unscathed" in {
      val inputXml, expectedXml =
        <html>
          <tag>value</tag>
          <list>
            <monkey>1</monkey>
            <monkey>2</monkey>
          </list>
        </html>

      new Parser(DomainBridge).parse(inputXml) must be equalTo expectedXml
    }
  }

  val UNPROCESSED_HTML =
    <html>
      <table>
        <tr>
          <td>Mixins</td>
          <td>Bootstrap</td>
        </tr>
      </table>
      <table>
        <tr>
          <td>Mirror</td>
          <td>Hello</td>
          <td>becomes</td>
          <td>Sausage</td>
        </tr>
      </table>
    </html>

  val PROCESSED_HTML =
    <html>
      <table>
        <tr>
          <td class="Setup">Mixins</td>
          <td class="Setup">Bootstrap</td>
        </tr>
      </table>
      <table>
        <tr>
          <td class="Setup">Mirror</td>
          <td class="Pass">Hello</td>
          <td class="Setup">becomes</td>
          <td class="Fail">Sausage</td>
        </tr>
      </table>
    </html>

  "html with tables" should {
    "have pass and fail classes janged into it" in {
      new Parser(DomainBridge).parse(UNPROCESSED_HTML) must be equalTo PROCESSED_HTML
    }
  }

  "it" should {
    "display exceptions in roll-ups, so that the page size doesn't change" in {
      failure
    }
    "display expected vs actual in roll-ups or summat" in {
      failure
    }
  }
}