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

  "html with tables" should {
    "have pass and fail classes janged into it" in {
      val input =
        <html>
          <table>
              <tr>
                  <td>Mixins</td>
                  <td>Bootstrap</td>
              </tr>
          </table>
          <table>
            <tr><td>Hello</td></tr>
            <tr><td>Sausage</td></tr>
          </table>
        </html>

      val expectedOutput =
        <html>
          <table>
              <tr>
                  <td class="Setup">Mixins</td>
                  <td class="Setup">Bootstrap</td>
              </tr>
          </table>
          <table>
            <tr><td class="Pass">Hello</td></tr>
            <tr><td class="Fail">Sausage</td></tr>
          </table>
        </html>

      new Parser(DomainBridge).parse(input) must be equalTo expectedOutput
    }
  }
}