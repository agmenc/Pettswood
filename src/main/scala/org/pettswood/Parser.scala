package org.pettswood

import xml._

class Parser(domain: DomainBridge) {

  def parse(test: Node): Node = traverseNodes(test)

  def deepCopy(elem: Elem, attributes: (Elem) => MetaData = _.attributes, text: String = ""): Elem = elem.copy(elem.prefix, elem.label, attributes(elem), TopScope, children(elem, text))

  def cssAdder(className: String): (Elem) => MetaData = {
    (elem: Elem) => {
      val classes = (elem \ "@class").mkString match {
        case "" => className
        case x => x + " " + className
      }
      new UnprefixedAttribute("class", classes, Null)
    }
  }

  def children(node: Node, textToAdd: String): NodeSeq = NodeSeq.fromSeq(node.child).map(child => traverseNodes(child))
  def firstCell(nodeSeq: NodeSeq): Elem = (nodeSeq \\ "td").head match { case elem: Elem => elem }

  def traverseNodes(currentNode: Node): Node = {
    currentNode match {
      case elem: Elem => elem.label match {
        case "table" => domain.table(firstCell(elem).text); deepCopy(elem)
        case "tr" => domain.row(); deepCopy(elem)
        case "td" => val result = domain.cell(elem.text); deepCopy(elem, cssAdder(result.name))
        case _ => deepCopy(elem)
      }
      case any => any
    }
  }
}