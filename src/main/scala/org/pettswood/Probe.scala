package org.pettswood

trait Probe
trait Doer extends Probe
trait Digger extends Probe {
  def result: String
}

case class NoProbe(text: String) extends Probe