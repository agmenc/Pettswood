package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._

class EndToEndSpec extends Specification with Mockito {
  "it" should {
    "work" in {
      new Runner(new Parser(DomainBridge),  new FileSystem).run("src/test/resources/What Is Pettswood?.html")

      failure
    }
  }
}