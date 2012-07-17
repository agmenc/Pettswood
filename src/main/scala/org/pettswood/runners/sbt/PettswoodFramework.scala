package org.pettswood.runners.sbt

import _root_.org.scalatools.testing._
import org.pettswood.runners.DefaultRunner
import org.pettswood.files._

class PettswoodFramework extends Framework {
  def name() = "Pettswood"
  def tests = Array[Fingerprint](new PettswoodFingerprint(false), new PettswoodFingerprint(true))
  def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new Sbt(classLoader, loggers, DefaultRunner, DefaultPettswoodFiles)
}

class PettswoodFingerprint(override val isModule: Boolean) extends SubclassFingerprint {
  def superClassName = "org.pettswood.runners.sbt.SbtIntegrationHook"
}

trait SbtIntegrationHook