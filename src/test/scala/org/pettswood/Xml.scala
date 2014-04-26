package org.pettswood

import scala.xml.NodeSeq

object Xml {
  // StringBaseMatchers.==/ is taking precedence over XmlMatchers.==/ even where StringBaseMatchers is not directly referenced anywhere in the test
  def equalStructurally(ns: NodeSeq) = org.specs2.matcher.XmlMatchers.be_==/(ns)
  def equalExactly(ns: NodeSeq) = org.specs2.matcher.StringMatchers.be_==/(ns.toString())
}