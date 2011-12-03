package org.pettswood.runners.sbt

import _root_.org.scalatools.testing._

trait PettswoodFingerprint extends SubclassFingerprint {
  def superClassName = "org.pettswood.runners.SbtIntegrationHook"
}