package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import parsers.xml.scala.Parser
import runners.DisposableRunner
import scala.xml.Node

class DisposableRunnerSpec extends SpecificationWithJUnit with Mockito {
  
  class Fixture {
    val domain = mock[DomainBridge]
    val parser = mock[Parser]
    val fileSystem = mock[FileSystem]
    val saver = mock[Saver]
    val finder = mock[Finder]
    val runner = new DisposableRunner(parser, fileSystem)
    fileSystem.loadXml(any[String]) returns <input></input>
    fileSystem.save(any[String]) returns saver
    fileSystem.in(any[String]) returns finder
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

      there was one(fixture.saver).to("target/pettswood/category/some.file")
    }
    "Tell the parser to decorate the output results" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.parser).decorate(<output></output>)
    }
    "Write the CSS file from the pettswood jar into the test src directory" in {
      val fixture = new Fixture
      fixture.fileSystem.loadResource(any[String]) returns "body {color: blue}"
      fixture.finder.find(any[String]) returns Nil

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.fileSystem).loadResource("css/pettswood.css")
      there was one(fixture.saver).to("src/test/resources/css/pettswood.css")
    }
    "Not write the CSS file if it is already there" in {
      val fixture = new Fixture
      fixture.finder.find(any[String]) returns List("pettswood.css")
      fixture.fileSystem.loadResource(any[String]) returns "body {color: blue}"

      fixture.runner run ("src/test/resources/category/some.file")

      there was no(fixture.fileSystem).loadResource("pettswood.css")
      there was no(fixture.saver).to("src/test/resources/pettswood.css")
    }
    "Copy the CSS file from the src directory to the target directory" in {
      val fixture = new Fixture
      fixture.finder.find(any[String]) returns Nil

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.fileSystem).copy("src/test/resources/css/pettswood.css", "target/pettswood/css/pettswood.css")
    }
    "Not copy the CSS file if it is already there" in {
      val fixture = new Fixture
      fixture.finder.find(any[String]) returns List("pettswood.css")

      fixture.runner run ("src/test/resources/category/some.file")

      there was no(fixture.fileSystem).copy("src/test/resources/pettswood.css", "target/pettswood/pettswood.css")
    }
  }
}