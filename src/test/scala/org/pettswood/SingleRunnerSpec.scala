package org.pettswood

import org.specs2.mutable.Specification
import runners.SingleRunner
import org.specs2.matcher.MatchResult

class SingleRunnerSpec extends Specification {
  "I can haz test" in pettswood("src/test/resources/MyFirstTest.html")
  "I can haz test" in pettswood("src/test/resources/Pettswood.html")
  "I can haz test" in pettswood("src/test/resources/PoorlyFormedXml.html")

  def pettswood(path: String): MatchResult[Boolean] = SingleRunner(path).overallPass must beTrue
}