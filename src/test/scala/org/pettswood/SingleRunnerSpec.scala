package org.pettswood

import org.specs2.mutable.Specification
import runners.SingleRunner

class SingleRunnerSpec extends Specification {
  "Frameworks such as Specs2 can run your test" in {
    SingleRunner("src/test/resources/OverworkedExample.html").overallPass must beTrue
  }
  "Frameworks such as Specs2 can run your test" in {
    SingleRunner("src/test/resources/OverworkedExample_out.html").overallPass must beTrue
  }
}