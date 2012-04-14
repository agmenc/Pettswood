package org.pettswood.specification.concepts

case class Mirror(input: String) {
  def reflection = input.reverse
}

object EmptyMirror extends Mirror("")