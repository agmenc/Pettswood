package org.pettswood.parsers.xml.scala

import org.custommonkey.xmlunit.{DetailedDiff, XMLUnit}
import scala.xml._
import scala.collection.JavaConverters._
import org.pettswood._
import java.io._
import java.util.UUID

import org.pettswood.Exception
import org.pettswood.Fail

class Parser(domain: DomainBridge) {

  def parse(node: Node): Node = new TestParser().traverse(node)

  // TODO - CAS - 14/10/2018 - Refactor HTML display logic
  // - Create a peer for parseCopy that doesn't also parse the children. parseCopy -> decorate, adding: decorateAndStop
  // - Extract the prettyPrinter
  // - Use an HTML diff to show tree-level diffs inline (hint: a tree is also a sequence, if you assume an ordering rule)
  class TestParser extends TraverseCopy {
    def traverse(node: Node): Node = node match {
      case elem: Elem => elem.label match {
        case "caption" =>
          val result = domain.table(elem.text); parseCopy(elem, extraContent = describeTableFailures(elem.text, result))

        case "th" => domain.header(elem.text); parseCopy(elem)
        case "tr" =>
          val headerRow = (elem \ "th").nonEmpty
          if (!headerRow) domain.newRow()
          try {
            parseCopy(elem)
          } finally {
            if (!headerRow) domain.rowEnd()
          }

        case "td" if (elem \ "section").nonEmpty =>
          val pp = new scala.xml.PrettyPrinter(0, 0)
          val expected = pp.format((elem \ "section").head).trim
          val result = domain.cell(expected)
          elem.copy(elem.prefix, elem.label, cssAdder(result.name)(elem), TopScope, elem.label != "script", describeCellHtmlFailures(expected, result))
        case "td" if (elem \\ "table").nonEmpty => domain.cell("Nested Table"); <td>{new Parser(domain.nestedDomain()).parse(<div>{NodeSeq.fromSeq(elem.child)}</div>)}</td>
        case "td" => val result = domain.cell(elem.text); parseCopy(elem, cssAdder(result.name), describeCellFailures(elem.text, result))
        case _ => parseCopy(elem)
      }
      case any => any
    }

    val pp = new scala.xml.PrettyPrinter(0, 0)

  }

  def cssAdder(className: String): Elem => MetaData = {
    elem: Elem => {
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

  def describeCellHtmlFailures(expected: String, result: Result): NodeSeq = {
    result match {
      case Fail(actual) =>
        <div style="border-style: solid; border-color: red;">
          <ul>
            {val diff: DetailedDiff = new DetailedDiff(XMLUnit.compareXML(expected, actual))
          diff.getAllDifferences.asScala.map { d =>
            <li>
              {d.toString}
            </li>
          }}
          </ul>
          <span>in</span>
          <xmp>{XML.loadString(actual)}</xmp>
          <span>vs</span>
          <xmp>{XML.loadString(expected)}</xmp>
        </div>
      case Exception(t: Throwable) => <span>
        {t.getMessage}
      </span> //TODO: prettifyException(t, UUID.randomUUID())
      case _ => XML.loadString(expected)
    }
  }

  def firstCell(nodeSeq: NodeSeq): Elem = {
    val tableCells = nodeSeq flatMap (_.descendant) filter (elem => elem.label == "th" || elem.label == "td" || elem.label == "caption")
    tableCells.head.asInstanceOf[Elem]
  }

  // TODO - replace overall with calls to summary
  def overall(summary: ResultSummary) = if (summary.overallPass) "Pass" else "Fail"
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
