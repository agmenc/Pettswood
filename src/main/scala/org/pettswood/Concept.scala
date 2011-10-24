package org.pettswood

abstract class Concept {
  var probeLibrary: String => String => Probe = { case _ => NoProbe }
  def row() {}
  def cell(text: String): Result
}

object NoConcept extends Concept {
  def cell(text: String) = Exception("The NoConcept Concept supports no events", "No stack trace required")
}

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def result: String
}

case class NoProbe(text: String) extends Probe

class Grouper(domain: DomainBridge)