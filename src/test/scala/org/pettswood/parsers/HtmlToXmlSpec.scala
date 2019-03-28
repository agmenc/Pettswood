package org.pettswood.parsers

import org.specs2.mutable.Specification
import scala.xml.NodeSeq
import org.pettswood.Xml._

class HtmlToXmlSpec extends Specification {
  "XML entities are converted to unicode, so that Scala can parse them" in {
    val input = "<div>&larr;&rarr;</div>"

    val result = HtmlToXml(input)

    result must equalExactly (wrap(<div>←→</div>))
  }

  "<br> tags are giving a closing /" in {
    val input = "<br>"

    val result = HtmlToXml(input)

    result must equalExactly (wrap(<br/>))
  }

  "captions are allowed /" in {
    val input = "<table><caption>head</caption><tr><td>hi</td></tr></table>"

    val result = HtmlToXml(input)

    result must equalExactly (wrap(<table><caption>head</caption><tbody><tr><td>hi</td></tr></tbody></table>))
  }

  def wrap(ns: NodeSeq): NodeSeq = <html><head/><body>{ns}</body></html>
}