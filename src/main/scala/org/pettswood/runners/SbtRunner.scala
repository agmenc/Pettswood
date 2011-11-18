package org.pettswood.runners

import _root_.org.scalatools.testing._
import Fingerprints._
import org.pettswood.{ResultSummary, DomainBridge, Parser, FileSystem}

class PettswoodFramework extends Framework with SbtIntegrationHook {
  def name() = "Pettswood"
  def tests = Array[Fingerprint](fp1, fp2)
  def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new SbtRunner(classLoader, loggers)
}

object Fingerprints {
  val fp1 = new PettswoodFingerprint {def isModule = false}
  val fp2 = new PettswoodFingerprint {def isModule = true}
}

trait PettswoodFingerprint extends SubclassFingerprint {
  def superClassName = "org.pettswood.runners.SbtIntegrationHook"
}

trait SbtIntegrationHook

class SbtRunner(loader: ClassLoader, val loggers: Array[Logger]) extends Runner2 with EventHandling {
  def log(f: Logger => Unit) {
    loggers.foreach {f}
  }

  def logResults(summary: ResultSummary) {
    summary.overallPass match {
      case true => log {logger => logger.info(summary.toString)}
      case false => log {logger => logger.error(summary.toString)}
    }
  }

  def event(filePath: String, summary: ResultSummary) = if (summary.overallPass) Pass(filePath) else Fail(filePath)

  // TODO - make summary accessible via Parser
  // TODO - make summary match an overall result type, i.e. Pass, Fail, Skip
  def run(testClassName: String, fingerprint: Fingerprint, eventHandler: EventHandler, args: Array[String]) {
    for (filePath <- (new FileSystem) in "src/test/resources" find ".*.html") {
      val domain = new DomainBridge
      log(_.info("Pettswood test ==> " + filePath))
      new org.pettswood.Runner(new Parser(domain), new FileSystem).run(filePath)
      eventHandler.handle(event(filePath, domain.summary))
      logResults(domain.summary)
    }
  }
}

trait EventHandling {
  abstract class SbtEvent(name: String, actualResult: Result) extends Event {
    def testName = name
    def description = ""
    def result = Result.Success
    def error: Throwable = null
  }
  case class Pass(name: String) extends SbtEvent(name, Result.Success)
  case class Fail(name: String) extends SbtEvent(name, Result.Failure)
}
