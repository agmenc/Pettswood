package org.pettswood.runners.sbt

import _root_.org.scalatools.testing._
import org.pettswood.{ResultSummary, FileSystem}
import org.pettswood.runners.RecycleableRunner

class Sbt(loader: ClassLoader, val loggers: Array[Logger], runner: RecycleableRunner) extends Runner2 with EventHandling {
  def log(f: Logger => Unit) { loggers.foreach {f} }

  def logResults(summary: ResultSummary, filePath: String) {
    summary.overallPass match {
      case true => log {logger => logger.info(" " + summary.toString + " ==> " + filePath)}
      case false => log {logger => logger.error(summary.toString + " ==> " + filePath)}
    }
  }

  def run(testClassName: String, fingerprint: Fingerprint, eventHandler: EventHandler, args: Array[String]) {
    log {logger => logger.info(" ")}
    for (filePath <- (new FileSystem) in "src/test/resources" find ".*.html") {
      runSingle(filePath, eventHandler)
    }
    log {logger => logger.info(" ")}
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