package org.pettswood.runners.sbt

import _root_.org.scalatools.testing._
import org.pettswood.ResultSummary

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
