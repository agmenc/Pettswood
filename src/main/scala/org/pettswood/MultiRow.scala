package org.pettswood

import org.pettswood.Result._

trait MultiRow extends Concept {
  
  def columns: PartialFunction[String, String => Probe]
  def initialiseRow() {}
  def rowComplete() {}
  def tableComplete() {}

  override def initHeader(headerText: String): Result = {
    probeTemplate = probeFor(headerText) :: probeTemplate; Uninteresting()
  }

  var currentProbes, probeTemplate = List.empty[String => Probe]

  override final def initRow() {
    currentProbes = probeTemplate.reverse
    initialiseRow()
  }

  override final  def endRow() {
    rowComplete()
  }

  override final  def endTable() {
    tableComplete()
  }

  def cell(cellText: String): Result = probeForCell(cellText) match {
    case descriptor: Descriptor => Description()
    case doer: Doer => Setup()
    case digger: Digger => given(cellText, digger.actual)
  }

  private def probeForCell(cellText: String): Probe = try { currentProbes.head(cellText) } finally {
    currentProbes = currentProbes.tail
  }

  private def probeFor(text: String): String => Probe = try {
    columns(text)
  } catch {
    case e: MatchError => throw new RuntimeException("Unrecognised column probe: " + text)
    case e: NullPointerException => throw new RuntimeException("No column probes defined")
  }
}