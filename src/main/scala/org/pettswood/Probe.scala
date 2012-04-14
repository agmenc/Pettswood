package org.pettswood

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def actual: String
}

case class DoNothingProbe(text: String) extends Doer