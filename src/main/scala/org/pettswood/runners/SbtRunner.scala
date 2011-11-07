package org.pettswood.runners

import _root_.org.scalatools.testing._
import Fingerprints._
import org.pettswood.{DomainBridge, Parser, FileSystem}

class PettswoodFramework extends Framework {
  def name() = "Pettswood"
  def tests = Array[Fingerprint](fp1, fp2)
  def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new SbtRunner(classLoader, loggers)
}

object Fingerprints {
  val fp1 = new PettswoodFingerprint {def isModule = false}
  val fp2 = new PettswoodFingerprint {def isModule = true}
}

trait PettswoodFingerprint extends TestFingerprint {
  def superClassName = "org.pettswood.runners.ReplaceThisWithSbtRunner"
}

class SbtRunner(loader: ClassLoader, val loggers: Array[Logger]) extends Runner2 with EventHandling {
  def log(f: Logger => Unit) {
    loggers.foreach {f}
  }

  def run(testClassName: String, fingerprint: Fingerprint, eventHandler: EventHandler, args: Array[String]) {
    for (filePath <- (new FileSystem) in "src/test/resources" find ".*.html") {
      val domain = new DomainBridge
      new org.pettswood.Runner(new Parser(domain), new FileSystem).run(filePath)
      domain.summary.overallPass match {
        case true => eventHandler.handle(pass(filePath)); log {_.info(domain.summary.toString)}
        case false => eventHandler.handle(fail(filePath)); log {_.error(domain.summary.toString)}
      }
    }
  }
}

trait EventHandling {
  class TheEvent(name: String) extends Event {
    def testName = name
    def description = "What is the description used for?"
    def result = Result.Success
    def error: Throwable = null
  }
  def pass(name: String) = new TheEvent(name)
  def fail(name: String) = new TheEvent(name) { override def result = Result.Failure }
  def exception(name: String) = new TheEvent(name) { override def result = Result.Error }
}

class ReplaceThisWithSbtRunner