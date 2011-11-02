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

      new Parser(new DomainBridge).parse(inputXml) must be equalTo expectedXml
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
          <td>What about an extra cell?</td>
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
          <td class="Fail"><span class="result">olleH<br></br>but expected:<br></br></span>Sausage</td>
          <td class="Exception"><span class="result">Unhandled state: List(What about an extra cell?, Sausage, becomes, Hello)<br></br>Expected:<br></br></span>What about an extra cell?</td>
        </tr>
      </table>
    </html>

  "For HTML with tables, the parser should" should {
    "pump out suitably modified results" in {
      new Parser(new DomainBridge).parse(UNPROCESSED_HTML) must be equalTo PROCESSED_HTML
    }
  }
}