package org.pettswood

import org.specs2.mutable.Specification
import runners.SingleRunner

class SingleRunnerSpec extends Specification {
  "Frameworks such as Specs2 can run your test" in {
    SingleRunner("src/test/resources/Pettswood.html").overallPass must beTrue
  }
}