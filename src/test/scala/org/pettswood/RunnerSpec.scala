package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import xml.Node
import util.Properties._
import java.io.File

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
    parser.decorate(any[Node]) returns <decorated></decorated>
  }

  "Constructing the runner" should {
    "load the test file into the DomainBridge" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.parser).parse(<input></input>)
    }
    "Write the result of the test into the output file" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.fileSystem).save(<decorated></decorated> toString())
    }
    "Put the output file heirarchy in target/pettswood" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.saver).to(userDir + File.separator + "target/pettswood/category/some.file")
    }
    "Tell the parser to decorate the output results" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.parser).decorate(<output></output>)
    }
  }
}