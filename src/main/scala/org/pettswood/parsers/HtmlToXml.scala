package org.pettswood.parsers

import scala.xml._
import org.htmlcleaner.{PrettyHtmlSerializer, HtmlCleaner, CleanerProperties}

object HtmlToXml {
  def apply(input: String): Node = {
    val props = new CleanerProperties()
    props.setOmitXmlDeclaration(true)
    props.setUseEmptyElementTags(false)
    props.setTranslateSpecialEntities(true)
    val tagNode = new HtmlCleaner(props).clean(input)
    val asString = new PrettyHtmlSerializer(props).getAsString(tagNode)
    XML.loadString(asString)
  }
}