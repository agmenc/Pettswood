package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._

class EndToEndSpec extends Specification with Mockito {
  "it" should {
    "work" in {
      val domain = new DomainBridge

      new Runner(new Parser(domain),  new FileSystem).run("src/test/resources/What Is Pettswood?.html")

      domain.summary.overallPass must be equalTo false
    }
  }
}