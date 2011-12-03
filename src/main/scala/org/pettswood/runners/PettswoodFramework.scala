package org.pettswood.runners

import _root_.org.scalatools.testing._
import sbt.Fingerprints._
import sbt.Sbt

class PettswoodFramework extends Framework {
  def name() = "Pettswood"
  def tests = Array[Fingerprint](fp1, fp2)
  def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new Sbt(classLoader, loggers, DefaultRunner)
}

trait SbtIntegrationHook