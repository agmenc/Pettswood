package org.pettswood

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def actual: String
}

case class DoNothing(text: String) extends Doer
case class NotImplemented(text: String) extends Digger {
  def actual = "Not implemented"
}
