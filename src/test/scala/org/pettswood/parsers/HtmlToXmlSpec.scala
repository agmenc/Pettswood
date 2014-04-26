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

  def wrap(ns: NodeSeq): NodeSeq = <html><head/><body>{ns}</body></html>
}