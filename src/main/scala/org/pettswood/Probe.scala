package org.pettswood

sealed trait Probe
trait Descriptor extends Probe
trait Doer extends Probe
trait Digger extends Probe {
  def actual: String
}

case class Describe(text: String) extends Descriptor
case class DoNothing(text: String) extends Doer
case class NotImplemented(text: String) extends Digger {
  def actual = "Not implemented"
}
