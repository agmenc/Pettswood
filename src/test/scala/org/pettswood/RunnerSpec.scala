package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import xml.Node

class RunnerSpec extends SpecificationWithJUnit with Mockito {

  class Fixture {
    val domain = mock[DomainBridge]
    val parser = mock[Parser]
    val fileSystem = mock[FileSystem]
    val saver = mock[Saver]
    val runner = new Runner(parser, fileSystem)
    fileSystem.load(any[String]) returns <input></input>
    fileSystem.save(any[String]) returns saver
    parser.parse(any[Node]) returns <output></output>
  }

  "Constructing the runner" should {
    "load the test file into the DomainBridge" in {
      val fixture = new Fixture

      fixture.runner run ("path/to/some.file")

      there was one(fixture.parser).parse(<input></input>)
    }
    "Write the result of the test into the output file" in {
      val fixture = new Fixture

      fixture.runner run ("path/to/some.file")

      there was one(fixture.fileSystem).save(<output></output> toString())
    }
  }
}