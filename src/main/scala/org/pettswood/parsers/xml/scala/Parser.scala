package org.pettswood.parsers.xml.scala

import scala.xml._
import org.pettswood._
import java.io._
import org.pettswood.Exception
import org.pettswood.Fail

class Parser(domain: DomainBridge) {

  def parse(node: Node): Node = new TestParser().traverse(node)

  class TestParser extends TraverseCopy {
    def traverse(node: Node) = node match {
      case elem: Elem => elem.label match {
        case "table" => val result = domain.table(firstCell(elem).text); parseCopy(elem, cssAdder(result.name), describeTableFailures(elem.text, result))
        case "tr" => domain.row(); parseCopy(elem)
        case "td" if (elem \\ "table").nonEmpty => domain.cell("Nested Table"); <td>{new Parser(domain.nestedDomain()).parse(<div>{NodeSeq.fromSeq(elem.child)}</div>)}</td>
        case "td" => val result = domain.cell(elem.text); parseCopy(elem, cssAdder(result.name), describeCellFailures(elem.text, result))
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

  def describeCellFailures(expectedText: String, result: Result) = {
    result match {
      case Fail(actual) => <span class="result">{actual}<br></br>but expected:<br></br></span>
      // TODO - CAS - 15/04/2014 - Make this a link to a separate doc, to make files more readable and smaller
      case Exception(t: Throwable) => <span class="result">{exceptionTrace(t)}<br></br>Expected:<br></br></span>
      case _ => NodeSeq.Empty
    }
  }

  def exceptionTrace(t: Throwable) = {
    val writer = new StringWriter()
    t.printStackTrace(new PrintWriter(writer))
    writer.toString
  }

  def describeTableFailures(expectedText: String, result: Result) = {
    val failureMarker = describeCellFailures(expectedText, result)
    if (failureMarker.isEmpty) failureMarker else <tr><td>{failureMarker}</td></tr>
  }

  def firstCell(nodeSeq: NodeSeq): Elem = (nodeSeq \\ "td").head match { case elem: Elem => elem }

  // TODO - replace overall with calls to summary
  def overall(summary: ResultSummary) = if(summary.overallPass) "Pass" else "Fail"
  def summary = domain.summary

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