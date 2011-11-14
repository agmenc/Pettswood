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
          <td class="Exception"><span class="result">java.lang.IllegalStateException: Unhandled state: List(What about an extra cell?, Sausage, becomes, Hello)<br></br>Expected:<br></br></span>What about an extra cell?</td>
        </tr>
      </table>
    </html>

  "For HTML with tables, the parser should" should {
    "pump out suitably modified results" in {
      // TODO - again, children of the result are in a NodeSeq, while children of the expected are in an ArrayBuffer. Create a better equals? A better XML library?
      new Parser(new DomainBridge).parse(UNPROCESSED_HTML).toString() must be equalTo (PROCESSED_HTML.toString())
    }
  }

  val NESTED_TABLE =
    <html>
      <table>
        <tr>
          <td>Mixins</td>
          <td>Bootstrap</td>
        </tr>
      </table>
      <table>
        <tr class="fixture">
          <td>Pettswood</td>
        </tr>
        <tr class="names">
          <td>Result</td>
        </tr>
        <tr>
          <td>
            <table>
              <tr class="fixture">
                <td>Results</td>
              </tr>
              <tr class="names">
                <td>pass</td>
                <td>fail</td>
                <td>setup</td>
                <td>exception</td>
              </tr>
              <tr>
                <td>4</td>
                <td>0</td>
                <td>8</td>
                <td>0</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </html>

  val PROCESSED_NESTED_HTML =
    <html>
      <table>
        <tr>
          <td class="Setup">Mixins</td>
          <td class="Setup">Bootstrap</td>
        </tr>
      </table>
      <table>
        <tr class="fixture">
          <td class="Setup">Pettswood</td>
        </tr>
        <tr class="names">
          <td class="Setup">Result</td>
        </tr>
        <tr>
          <td><div>
            <table>
              <tr class="fixture">
                <td class="Setup">Results</td>
              </tr>
              <tr class="names">
                <td class="Setup">pass</td>
                <td class="Setup">fail</td>
                <td class="Setup">setup</td>
                <td class="Setup">exception</td>
              </tr>
              <tr>
                <td class="Pass">4</td>
                <td class="Pass">0</td>
                <td class="Pass">8</td>
                <td class="Pass">0</td>
              </tr>
            </table>
          </div></td>
        </tr>
      </table>
    </html>

  "Nested tables" should {
    "work" in {
      new Parser(new DomainBridge).parse(NESTED_TABLE).toString() must be equalTo (PROCESSED_NESTED_HTML.toString())
    }
  }
}