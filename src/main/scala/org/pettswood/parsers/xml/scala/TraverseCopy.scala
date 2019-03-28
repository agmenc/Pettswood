package org.pettswood.parsers.xml.scala

import xml._

abstract class TraverseCopy {

  def traverse(node: Node): Node

  private def append(extra: NodeSeq, childNodes: NodeSeq): NodeSeq = extra match {
    case NodeSeq.Empty => childNodes
    case x: NodeSeq => appendExtras(x, childNodes)
  }

  def appendExtras(extraContent: NodeSeq, kids: NodeSeq): NodeSeq = extraContent.head +: kids

  def parseCopy(elem: Elem, attributes: Elem => MetaData = _.attributes, extraContent: NodeSeq = NodeSeq.Empty): Elem =
    elem.copy(elem.prefix, elem.label, attributes(elem), TopScope, elem.label != "script", append(extraContent, children(elem)))

  private def children(node: Node): NodeSeq = NodeSeq.fromSeq(node.child).map(child => traverse(child))
}