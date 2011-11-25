package org.pettswood

import scala.xml._
import xml.TraverseCopy

class Parser(domain: DomainBridge) {

  def parse(node: Node): Node = new TestParser().traverse(node)

  class TestParser extends TraverseCopy {
    def traverse(node: Node) = node match {
      case elem: Elem => elem.label match {
        case "table" => domain.table(firstCell(elem).text); parseCopy(elem)
        case "tr" => domain.row(); parseCopy(elem)
        case "td" if((elem \\ "table").iterator.hasNext) => <td>{new Parser(domain.nestedDomain()).parse(<div>{NodeSeq.fromSeq(elem.child)}</div>)}</td>
        case "td" => val result = domain.cell(elem.text); parseCopy(elem, cssAdder(result.name), describeAnyFailures(elem.text, result))
        case _ => parseCopy(elem)
      }
      case any => any
    }
  }

  def cssAdder(className: String): (Elem) => MetaData = {
    (elem: Elem) => {
      val classes = (elem \ "@class").text match {
        case "" => className
        case x => x + " " + className
      }
      new UnprefixedAttribute("class", classes, Null)
    }
  }

  def describeAnyFailures(expectedText: String, result: Result) = {
    result match {
      case Fail(actual) => <span class="result">{actual}<br></br>but expected:<br></br></span>
      case Exception(exceptionText) => <span class="result">{exceptionText}<br></br>Expected:<br></br></span>
      case _ => NodeSeq.Empty
    }
  }

  def firstCell(nodeSeq: NodeSeq): Elem = (nodeSeq \\ "td").head match { case elem: Elem => elem }
  def overall(summary: ResultSummary) = if(summary.overallPass) "Pass" else "Fail"

  def decorate(node: Node): Node = new TestDecorator().traverse(node)

  class TestDecorator extends TraverseCopy {
    def traverse(node: Node) = node match {
      case elem: Elem => elem match {
        case <body>{contents@_*}</body> => parseCopy(elem, _.attributes,
          <table><tr><td class="Setup">Results:</td><td class={overall(domain.summary)}>{domain.summary.toString}</td></tr></table>)
        case anyElem => parseCopy(elem)
      }
      case other => other
    }

    override def appendExtras(extraContent: NodeSeq, kids: NodeSeq) = kids :+ extraContent.head
  }
}