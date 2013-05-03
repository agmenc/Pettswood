package org.pettswood.parsers

import org.specs2.mutable.Specification
import scala.xml.NodeSeq

class HtmlToXmlSpec extends Specification {
  "XML entities are converted to unicode, so that Scala can parse them" in {
    val input = "<div>&larr;&rarr;</div>"

    val result = HtmlToXml(input)

    result === wrap(<div>←→</div>)
  }

  "<br> tags are giving a closing /" in {
    val input = "<br>"

    val result = HtmlToXml(input)

    result === wrap(<br/>)
  }

  "Script tags retain their closing tag, so that browsers don't ignore them" in {
    val dodgyHtml = "" +
      "<html>" +
      "    <head>" +
      "        <script type=\"text/javascript\" src=\"javascript/jquery-1.7.2.min.js\"></script>" +
      "    </head>" +
      "</html>"

    val result = HtmlToXml(dodgyHtml)

    val expected =
      <html>
        <head>
          <script type="text/javascript" src="javascript/jquery-1.7.2.min.js"></script>
        </head>
        <body></body>
      </html>

    result === expected
  }

  def wrap(ns: NodeSeq): NodeSeq = <html><head/><body>{ns}</body></html>

  implicit class EquableNodeSeq(ns: NodeSeq) {
    def ===(other: NodeSeq) = ns must be_==/(other)
  }
}