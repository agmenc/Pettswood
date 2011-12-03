package org.pettswood.runners

import _root_.org.scalatools.testing._
import Fingerprints._
import org.pettswood.{ResultSummary, FileSystem}

class PettswoodFramework extends Framework with SbtIntegrationHook {
  def name() = "Pettswood"
  def tests = Array[Fingerprint](fp1, fp2)
  def testRunner(classLoader: ClassLoader, loggers: Array[Logger]) = new Sbt(classLoader, loggers, DefaultRunner)
}

object Fingerprints {
  val fp1 = new PettswoodFingerprint {def isModule = false}
  val fp2 = new PettswoodFingerprint {def isModule = true}
}

trait PettswoodFingerprint extends SubclassFingerprint {
  def superClassName = "org.pettswood.runners.SbtIntegrationHook"
}

trait SbtIntegrationHook

class Sbt(loader: ClassLoader, val loggers: Array[Logger], runner: RecycleableRunner) extends Runner2 with EventHandling with SbtIntegrationHook {
  def log(f: Logger => Unit) {
    loggers.foreach {f}
  }

  def logResults(summary: ResultSummary, filePath: String) {
    summary.overallPass match {
      case true => log {logger => logger.info(" " + summary.toString + " ==> " + filePath)}
      case false => log {logger => logger.error(summary.toString + " ==> " + filePath)}
    }
  }

  def run(testClassName: String, fingerprint: Fingerprint, eventHandler: EventHandler, args: Array[String]) {
    for (filePath <- (new FileSystem) in "src/test/resources" find ".*.html") {
      runSingle(filePath, eventHandler)
    }
  }

  // TODO - make summary match an overall result type, i.e. Pass, Fail, Skip
  def runSingle(filePath: String, eventHandler: EventHandler) {
    try {
      val summary = runner.run(filePath)
      val theEvent = event(summary, filePath)
      eventHandler.handle(theEvent)
      logResults(summary, filePath)
    }
    catch {
      case e => eventHandler.handle(Error(filePath, e)); log(_.error("  *** Failed to read test   *** ==> " + filePath)); log(_.trace(e))
    }
  }
}

trait EventHandling {
  trait SbtEvent extends Event {
    def name: String
    def actualResult: Result

    def testName = name
    def description = "SbtRunner result"
    def result = actualResult
    def error: Throwable = null
  }

  def event(summary: ResultSummary, filePath: String) = if (summary.overallPass) Pass(filePath) else Fail(filePath)
  case class Pass(name: String, actualResult: Result = Result.Success) extends SbtEvent
  case class Fail(name: String, actualResult: Result = Result.Failure) extends SbtEvent
  case class Error(name: String, e: Throwable, actualResult: Result = Result.Error) extends SbtEvent { override def error = e }
}
