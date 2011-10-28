package org.pettswood

import xml._

class Parser(domain: DomainBridge) {

  def parse(test: Node): Node = traverseNodes(test)

  def deepCopy(elem: Elem, attributes: (Elem) => MetaData = _.attributes): Elem = elem.copy(elem.prefix, elem.label, attributes(elem), TopScope, children(elem))

  def cssAdder(className: String): (Elem) => MetaData = {
    (elem: Elem) => new UnprefixedAttribute("class", className, Null)
  }

  def copyAndChange(elem: Elem, colour: String): Elem = deepCopy(elem, cssAdder(colour))

  def children(node: Node): NodeSeq = NodeSeq.fromSeq(node.child).map(child => traverseNodes(child))
  def firstCell(nodeSeq: NodeSeq): Elem = (nodeSeq \\ "td").head match { case elem: Elem => elem }

  def traverseNodes(currentNode: Node): Node = {
    currentNode match {
      case elem: Elem => elem.label match {
        case "table" => {domain.table(firstCell(elem).text); deepCopy(elem)}
        case "tr" => {domain.row(); deepCopy(elem)}
        case "td" => var result = domain.cell(elem.text); copyAndChange(elem, result.name)
        case _ => deepCopy(elem)
      }
      case any => any
    }
  }
}