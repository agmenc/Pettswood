package org.pettswood

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def result: String
}

case class DoNothingProbe(text: String) extends Probe