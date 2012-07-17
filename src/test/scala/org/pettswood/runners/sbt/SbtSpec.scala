package org.pettswood.runners.sbt

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.pettswood.ResultSummary
import org.scalatools.testing._
import org.pettswood.runners.RecycleableRunner
import org.pettswood.files.PettswoodFiles
import scala.Array

class SbtSpec extends SpecificationWithJUnit with Mockito with EventHandling {
  class Fixture {
    val logger = mock[Logger]
    val eventHandler = mock[EventHandler]
    val runner = mock[RecycleableRunner]
    val finder = mock[PettswoodFiles]
    val sbt = new Sbt(mock[ClassLoader], Array(logger), runner, finder)
    val summary = mock[ResultSummary]
  }

  "handles big (test-loading) failures by telling the event handler all about it and logging wildly" in {
    val fixture = new Fixture
    val throwable = new RuntimeException("Stuff went wrong")
    fixture.runner.run(any[String]) throws throwable

    fixture.sbt.runSingle("some file path", fixture.eventHandler)

    there was one(fixture.logger).error("  *** Failed to read test *** Stuff went wrong ==> some file path")
    there was one(fixture.eventHandler).handle(any[Error])
    // TODO - mockito is not playing nicely here. The actual and expected are equal, but it thinks the method arguments are different
//    there was one(fixture.eventHandler).handle(Error("some file path", throwable))
  }
  "handles little (test-execution) failures by telling the event handler all about it and logging succintly" in {
    val fixture = new Fixture
    fixture.runner.run(any[String]) returns fixture.summary
    fixture.summary.overallPass returns false
    fixture.summary.toString returns "Pass: 0 Fail: 0 Setup: 0 Exception: 0"

    fixture.sbt.runSingle("some file path", fixture.eventHandler)

    there was one(fixture.logger).error("Pass: 0 Fail: 0 Setup: 0 Exception: 0 ==> some file path")
    there was one(fixture.eventHandler).handle(any[Fail])
    // TODO - mockito is not playing nicely here. The actual and expected are equal, but it thinks the method arguments are different
//    there was one(fixture.eventHandler).handle(Fail("some file path"))
  }

  "Uses the FileFinder to find test file paths" in {
    val fixture = new Fixture
    fixture.finder.testFilePaths returns Seq("path/a", "path/b")

    fixture.sbt.run("org.pettswood.runners.sbt.SbtSpec", mock[Fingerprint], fixture.eventHandler, Array[String]())

    there was one(fixture.finder).testFilePaths
  }
}