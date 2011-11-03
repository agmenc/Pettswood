package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._

class EndToEndSpec extends Specification with Mockito {
  "it" should {
    "work" in {
      val domain = new DomainBridge
      new Runner(new Parser(domain),  new FileSystem).run("src/test/resources/What Is Pettswood?.html")

      domain.summary.overallPass must be equalTo false
      domain.summary.pass must be equalTo 9
      domain.summary.fail must be equalTo 4
      domain.summary.exception must be equalTo 9
      domain.summary.setup must be equalTo 37
    }
  }
}