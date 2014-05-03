package org.pettswood.files

import org.specs2.mutable._
import org.pettswood.PettswoodConfig._
import DefaultPettswoodFiles._

class PettswoodFilesSpec extends SpecificationWithJUnit {
  "We can specify multiple test HTML roots in the config" in {
    testSources ++= Seq("src/otherTests", "src/otherTests2")

    testFilePaths map relativise must contain("src/otherTests/bananas/Monkeys.html")
    testFilePaths map relativise must contain("src/otherTests2/burrito/Chargrilled.html")
  }

  def relativise(path: String) = path.replaceAll(".*src", "src")
}