package org.pettswood.runners.sbt

import _root_.org.scalatools.testing._
import org.pettswood.ResultSummary
import org.pettswood.runners.RecycleableRunner
import org.pettswood.files._

class Sbt(loader: ClassLoader, val loggers: Array[Logger], runner: RecycleableRunner, finder: PettswoodFiles) extends Runner2 with EventHandling {
  def log(f: Logger => Unit) { loggers.foreach(f) }
  def info(data: Any) { log { logger => logger.info(data.toString) } }
  def error(data: Any) { log { logger => logger.error(data.toString) } }

  def logResults(summary: ResultSummary, filePath: String) {
    summary.overallPass match {
      case true => info(" " + summary.toString + " ==> " + filePath)
      case false => log {logger => logger.error(summary.toString + " ==> " + filePath)}
    }
  }

  def run(testClassName: String, fingerprint: Fingerprint, eventHandler: EventHandler, args: Array[String]) {
    info(" ")
    processUserSettingsChanges(testClassName)
    for (filePath <- finder.testFilePaths) { runSingle(filePath, eventHandler) }
    info(" ")
  }

  def processUserSettingsChanges(testClassName: String) {
    try {
      val clazz = Class.forName(testClassName)
      val constructor = clazz.getDeclaredConstructors.head
      constructor.newInstance()
    } catch {
      case e: Throwable => error("  *** Could not instantiate config class: " + testClassName + " *** " + e); e.printStackTrace()
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
      case e: Throwable =>  eventHandler.handle(Error(filePath, e)); error("  *** Failed to read test *** " + e.getMessage + " ==> " + filePath)
    }
  }
}