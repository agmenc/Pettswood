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
    finder.find(any[String]) returns List.empty[String]
    parser.parse(any[Node]) returns <output></output>
    parser.decorate(any[Node]) returns <decorated></decorated>
  }

  "The runner" should {
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
      fixture.runner run ("src/acceptance/resources/features/another.file")

      there was one(fixture.saver).to("target/pettswood/category/some.file")
      there was one(fixture.saver).to("target/pettswood/features/another.file")
    }
    "Work out the output file in Windows" in {
      val fixture = new Fixture

      fixture.runner run ("src\\integration\\resources\\category\\some.file")

      there was one(fixture.saver).to("target/pettswood\\category\\some.file")
    }
    "Write the result of the test into the output file" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.fileSystem).save(<decorated></decorated> toString())
    }
    "Put the output file heirarchy in target/pettswood" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")
      fixture.runner run ("src/acceptance/resources/features/another.file")

      there was one(fixture.saver).to("target/pettswood/category/some.file")
      there was one(fixture.saver).to("target/pettswood/features/another.file")
    }
    "Tell the parser to decorate the output results" in {
      val fixture = new Fixture

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.parser).decorate(<output></output>)
    }
    "Extract CSS and javascript files from the pettswood jar into the test src directory" in {
      val fixture = new Fixture
      fixture.fileSystem.loadResource(any[String]) returns "body {color: blue}"
      fixture.finder.find(any[String]) returns Nil

      fixture.runner run ("src/test/resources/category/some.file")

      // TODO - CAS - 14/04/2012 - just load all resources in one go
      there was one(fixture.fileSystem).loadResource("css/pettswood.css")
      there was one(fixture.fileSystem).loadResource("javascript/jquery-1.7.2.min.js")
      there was one(fixture.fileSystem).loadResource("javascript/pettswood.js")
      there was one(fixture.saver).to("src/test/resources/css/pettswood.css")
      there was one(fixture.saver).to("src/test/resources/javascript/jquery-1.7.2.min.js")
      there was one(fixture.saver).to("src/test/resources/javascript/pettswood.js")
    }
    "Not extract CSS or javascript files if any are already there" in {
      val fixture = new Fixture
      fixture.finder.find(".*.css") returns List("a.css")
      fixture.finder.find(".*.js") returns List("x.js", "y.js")
      fixture.fileSystem.loadResource(any[String]) returns "xxx"

      fixture.runner run ("src/test/resources/category/some.file")

      there was no(fixture.fileSystem).loadResource("pettswood.css")
      there was no(fixture.fileSystem).loadResource("jquery-1.7.2.min.js")
      there was no(fixture.fileSystem).loadResource("pettswood.js")
      there was no(fixture.saver).to("src/test/resources/css/pettswood.css")
      there was no(fixture.saver).to("src/test/resources/javascript/jquery-1.7.2.min.js")
      there was no(fixture.saver).to("src/test/resources/javascript/pettswood.js")
    }
    "Copy all CSS files from the src directory to the target directory" in {
      val fixture = new Fixture
      fixture.finder.find(".*.css") returns List("a/b/c/src/test/resources/css/first.css", "a/b/c/src/test/resources/css/second.css")

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.fileSystem).copy("a/b/c/src/test/resources/css/first.css", "a/b/c/target/pettswood/css/first.css")
      there was one(fixture.fileSystem).copy("a/b/c/src/test/resources/css/second.css", "a/b/c/target/pettswood/css/second.css")
    }

    "Copy all Javascript files from the src directory to the target directory" in {
      val fixture = new Fixture
      fixture.finder.find(".*.js") returns List("a/b/c/src/test/resources/javascript/first.js", "a/b/c/src/test/resources/javascript/second.js")

      fixture.runner run ("src/test/resources/category/some.file")

      there was one(fixture.fileSystem).copy("a/b/c/src/test/resources/javascript/first.js", "a/b/c/target/pettswood/javascript/first.js")
      there was one(fixture.fileSystem).copy("a/b/c/src/test/resources/javascript/second.js", "a/b/c/target/pettswood/javascript/second.js")
    }
    "Not copy the CSS file if it is already there" in {
      val fixture = new Fixture
      fixture.finder.find(any[String]) returns List("pettswood.css")

      fixture.runner run ("src/test/resources/category/some.file")

      there was no(fixture.fileSystem).copy("src/test/resources/pettswood.css", "target/pettswood/pettswood.css")
    }
  }
}