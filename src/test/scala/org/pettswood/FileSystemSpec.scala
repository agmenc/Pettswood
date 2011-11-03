package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import io.Source._
import java.io.File
import org.specs2.specification.AfterExample
class FileSystemSpec extends SpecificationWithJUnit with Mockito with AfterExample {

  args(sequential = true)

  def after = {
    new File("./target/some.file").delete()
    new File("./target/a/very/nested/directory/structure/some.file").delete()
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
    "know how to find files of certain types" in {
      val fileSystem = new FileSystem

      fileSystem in "src/test" find "*.html" must be equalTo List("sausage.html", "monkey.html")
      fileSystem in "src/main/scala/org/" find "R*.scala" must be equalTo List("sausage.html", "monkey.html")
    }
  }
}