package org.pettswood

trait Concept {

  val columnProbes = Map.empty[String, Probe]

  // TODO - Make define() cases for Concepts part of case class constructors
  def define(probes: String => String => Probe) {
  }

  def cell(text: String)
}

object NoConcept extends Concept {
  def cell(text: String) { throw new RuntimeException("The NoConcept Concept supports no events") }
}

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def result: String
}

class Grouper(domain: DomainBridge)