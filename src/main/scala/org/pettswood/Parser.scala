package org.pettswood

import xml.{NodeSeq, TopScope, Elem, Node}

class Parser(domain: DomainBridge) {

  def parse(test: Node): Node = traverseNodes(test)
  def deepCopy(elem: Elem): Elem = elem.copy(elem.prefix, elem.label, elem.attributes, TopScope, children(elem).map(child => traverseNodes(child)))
  def children(currentNode: Node): NodeSeq = NodeSeq.fromSeq(currentNode.child)

  def traverseNodes(currentNode: Node): Node = {
    currentNode match {
      case elem: Elem => elem.label match {
        case "table" => { domain.table(); deepCopy(elem) }
        case "td" => { domain.cell(elem.text); deepCopy(elem) }
        case _ => deepCopy(elem)
      }
      case any => any
    }
  }
}
