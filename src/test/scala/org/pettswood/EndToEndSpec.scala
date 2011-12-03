package org.pettswood

import org.specs2.mutable.Specification
import org.specs2.mock._
import runners.DisposableRunner

class EndToEndSpec extends Specification with Mockito {
  "we can run a single test" in {
    val domain = new DomainBridge

    new DisposableRunner(new Parser(domain), new FileSystem).run("src/test/resources/What Is Pettswood?.html")

    domain.summary.overallPass must be equalTo false
  }
}