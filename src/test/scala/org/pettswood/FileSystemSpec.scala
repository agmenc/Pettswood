package org.pettswood

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock._
import io.Source._

class FileSystemSpec extends SpecificationWithJUnit with Mockito {

  // TODO - Do some tidy-up to clean away the test files
  "The filesystem" should {
    "Write output files" in {
      val fileSystem = new FileSystem

      fileSystem save ("some data") to ("./target/some.file")

      fromFile("./target/some.file").mkString must be equalTo "some data"
    }
    "make sure that the target folder exists when writing a file" in {
      val fileSystem = new FileSystem

      fileSystem save ("some data") to ("./target/a/very/nested/directory.structure/some.file")

      fromFile("./target/a/very/nested/directory.structure/some.file").mkString must be equalTo "some data"
    }
  }
}