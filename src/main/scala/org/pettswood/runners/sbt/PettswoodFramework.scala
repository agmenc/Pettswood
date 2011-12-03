package org.pettswood.runners.sbt

import _root_.org.scalatools.testing._
import Fingerprints._
import org.pettswood.runners.DefaultRunner

class PettswoodFramework extends Framework {
  def name() = "Pettswood"
  def tests = Array[Fingerprint](fp1, fp2)
  def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new Sbt(classLoader, loggers, DefaultRunner)
}