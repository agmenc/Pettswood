package org.pettswood

trait Concept {

  val columnProbes = Map.empty[String, Probe]

  // TODO - Make define() cases for Concepts part of case class constructors
  def columns(probes: String => String => Probe) {}

  def cell(text: String): Result
}

object NoConcept extends Concept {
  def cell(text: String)  = Exception("The NoConcept Concept supports no events", "No stack trace required")
}

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def result: String
}

class Grouper(domain: DomainBridge)