package org.pettswood.specification.concepts

import org.pettswood._

class MirrorFixture extends SimpleConcept {
  var mirror: Mirror = EmptyMirror

  def cell(text: String) = text match {
    case input if (mirror == EmptyMirror) => mirror = Mirror(input); Pass(input)
    case "becomes" => Setup()
    case expected => Result.given(expected, mirror.reflection)
  }
}