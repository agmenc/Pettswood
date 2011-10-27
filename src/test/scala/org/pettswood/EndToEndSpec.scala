package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._

class EndToEndSpec extends Specification with Mockito {
  "it" should {
    "work" in {
      new Runner(new Parser(DomainBridge),  new FileSystem).run("src/test/resources/What Is Pettswood?.html")

      DomainBridge.summary.overallPass must be equalTo false
      DomainBridge.summary.pass must be equalTo 12
      DomainBridge.summary.fail must be equalTo 2
      DomainBridge.summary.exception must be equalTo 0
      DomainBridge.summary.setup must be equalTo 19
    }
  }
}