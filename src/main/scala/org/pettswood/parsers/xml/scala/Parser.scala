package org.pettswood.parsers.xml.scala

import scala.xml._
import org.pettswood._
import java.io._
import java.util.UUID

import org.pettswood.Exception
import org.pettswood.Fail

class Parser(domain: DomainBridge) {

  def parse(node: Node): Node = new TestParser().traverse(node)

  class TestParser extends TraverseCopy {
    def traverse(node: Node) = node match {
      case elem: Elem => elem.label match {
        case "table" => val result = domain.table(firstCell(elem).text); parseCopy(elem, extraContent = describeTableFailures(elem.text, result))
        case "tr" => domain.row(); parseCopy(elem)
        case "td" if (elem \\ "table").nonEmpty => domain.cell("Nested Table"); <td>{new Parser(domain.nestedDomain()).parse(<div>{NodeSeq.fromSeq(elem.child)}</div>)}</td>
        case "td" | "th" => val result = domain.cell(elem.text); parseCopy(elem, cssAdder(result.name), describeCellFailures(elem.text, result))
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
      case Fail(actual) => <span>{actual}<br></br>but expected:<br></br></span>
      case Exception(t: Throwable) => prettifyException(t, UUID.randomUUID())
      case _ => NodeSeq.Empty
    }
  }

  private def prettifyException(t: Throwable, uuid: UUID) = {
    <div class="pettswoodExceptions">
      <span>
        <b>{t.getClass.getCanonicalName}</b>
        <a class="btn btn-danger btn-sm" data-toggle="collapse" href={s"#$uuid"} aria-expanded="false" aria-controls="collapseExample">
          Show/Hide
        </a>
      </span>
      <p>{t.getMessage}</p>
      <div class="collapse collapsar" id={s"#$uuid"}>
        <pre>{exceptionTrace(t)}</pre>
      </div>
    </div>
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

  def firstCell(nodeSeq: NodeSeq): Elem = {
    val tableCells = nodeSeq flatMap (_.descendant) filter (elem => elem.label == "th" || elem.label == "td")
    tableCells.head.asInstanceOf[Elem]
  }

  // TODO - replace overall with calls to summary
  def overall(summary: ResultSummary) = if(summary.overallPass) "Pass" else "Fail"
  def summary = domain.summary

  def decorate(node: Node): Node = new TestDecorator().traverse(node)

  class TestDecorator extends TraverseCopy {
    def traverse(node: Node) = node match {
      case elem: Elem => elem match {
        case <body>{contents@_*}</body> => parseCopy(elem, _.attributes,
          <div class="container"><div class="row"><table class="table"><tr><th>Results:</th><td class={overall(domain.summary)}>{domain.summary.toString}</td></tr></table></div></div>)
        case anyElem => parseCopy(elem)
      }
      case other => other
    }

    override def appendExtras(extraContent: NodeSeq, kids: NodeSeq) = kids :+ extraContent.head
  }
}