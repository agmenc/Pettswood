package org.pettswood.parsers

import scala.xml._
import org.htmlcleaner.{PrettyXmlSerializer, HtmlCleaner, CleanerProperties}

object HtmlToXml {
  def apply(input: String):Node = {
    val props = new CleanerProperties();
    props.setTranslateSpecialEntities(true);
    val tagNode = new HtmlCleaner(props).clean(input);
    val asString = new PrettyXmlSerializer(props).getAsString(tagNode)
    XML.loadString(asString)
  }
}