package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import io.Source._
import java.io.File
import org.specs2.specification.AfterExample
import util.Properties._

class FileSystemSpec extends SpecificationWithJUnit with Mockito with AfterExample {

  args(sequential = true)

  val BASE_PATH = userDir + File.separator

  def after = {
    List("./target/some.file",
      "./target/a/very/nested/directory/structure/some.file",
      "./target/pettswood/tmp1/monkeys.file",
      "./target/pettswood/tmp2/moreMonkeys.file"
    ) foreach (new File(_).delete())
    "Why does this procedure have to return Any(thing)?"
  }

  "The FileSystem proxy" should {
    "write output files" in {
      val fileSystem = new FileSystem

      fileSystem save ("some data") to ("./target/some.file")

      fromFile("./target/some.file").mkString must be equalTo "some data"
    }
    "make sure that the target folder exists when writing a file" in {
      val fileSystem = new FileSystem

      fileSystem save ("some data") to ("./target/a/very/nested/directory/structure/some.file")

      fromFile("./target/a/very/nested/directory/structure/some.file").mkString must be equalTo "some data"
    }
    "know how to find files by name regex" in {
      val fileSystem = new FileSystem

      fileSystem in "src/test" find ".*.html" must be equalTo List(
        BASE_PATH + "src/test/resources/Getting Started.html",
        BASE_PATH + "src/test/resources/Overworked Example.html",
        BASE_PATH + "src/test/resources/What Is Pettswood?.html"
      )
      
      fileSystem in "src/main/scala/" find "R.*.scala" must be equalTo List(
        BASE_PATH + "src/main/scala/org/pettswood/MultiRow.scala",
        BASE_PATH + "src/main/scala/org/pettswood/Result.scala",
        BASE_PATH + "src/main/scala/org/pettswood/ResultSummary.scala",
        BASE_PATH + "src/main/scala/org/pettswood/Runner.scala",
        BASE_PATH + "src/main/scala/org/pettswood/runners/SbtRunner.scala"
      )
    }
    "Convert relative paths to absolute" in {
      val fileSystem = new FileSystem

      fileSystem in "src/test" must be equalTo Finder(BASE_PATH + "src/test")

      fileSystem save ("some data") to "src/a.file"
      fromFile(BASE_PATH + "src/a.file").mkString must be equalTo "some data"
    }
    "Copy files" in {
      val fileSystem = new FileSystem
      fileSystem.save("some monkeys").to("target/pettswood/tmp1/monkeys.file")

      fileSystem.copy("target/pettswood/tmp1/monkeys.file", "target/pettswood/tmp2/moreMonkeys.file")
      
      fromFile(BASE_PATH + "target/pettswood/tmp2/moreMonkeys.file").mkString must be equalTo "some monkeys"
    }
  }
}