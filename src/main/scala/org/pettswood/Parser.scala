package org.pettswood

import xml._

class Parser(domain: DomainBridge) {

  def parse(test: Node): Node = traverseNodes(test)
  def deepCopy(elem: Elem): Elem = elem.copy(elem.prefix, elem.label, elem.attributes, TopScope, children(elem).map(child => traverseNodes(child)))
  def copyAndColour(elem: Elem, colour: String): Elem = elem.copy(elem.prefix, elem.label, new UnprefixedAttribute("class", colour, Null), TopScope, children(elem).map(child => traverseNodes(child)))
  def children(node: Node): NodeSeq = NodeSeq.fromSeq(node.child)
  def firstCell(nodeSeq: NodeSeq): Elem = (nodeSeq \\ "td").head match { case elem: Elem => elem }

  def traverseNodes(currentNode: Node): Node = {
    currentNode match {
      case elem: Elem => elem.label match {
        case "table" => {domain.table(firstCell(elem).text); deepCopy(elem)}
        case "tr" => {domain.row(); deepCopy(elem)}
        case "td" => var result = domain.cell(elem.text); copyAndColour(elem, result.name)
        case _ => deepCopy(elem)
      }
      case any => any
    }
  }
}
