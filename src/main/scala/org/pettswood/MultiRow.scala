package org.pettswood

import org.pettswood.Result._

trait MultiRow extends Concept {
  
  def columns: String => String => Probe
  def initialiseRow() {}

  var rowPointer = 0;
  var currentProbes, probeTemplate = List.empty[(String) => Probe]

  override def row() {
    rowPointer += 1
    currentProbes = probeTemplate.reverse
    initialiseRow()
  }

  def probeFor(text: String): (String) => Probe = try {
    columns(text)
  } catch {
    case e: MatchError => throw new RuntimeException("Unrecognised column probe: " + text)
    case e: NullPointerException => throw new RuntimeException("No column probes defined")
  }

  def cell(cellText: String) = rowPointer match {
    case 1 => Setup()
    case 2 => probeTemplate = probeFor(cellText) :: probeTemplate; Setup()
    case x => determineResult(cellText)
  }

  def determineResult(cellText: String): Result = {
    val probe = currentProbes.head(cellText)
    currentProbes = currentProbes.tail
    probe match {
      case doer: Doer => Setup()
      case digger: Digger => given(cellText, digger.actual)
    }
  }
}