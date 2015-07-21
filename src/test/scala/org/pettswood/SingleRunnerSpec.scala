package org.pettswood

import org.specs2.mutable.Specification
import runners.SingleRunner
import org.specs2.matcher.MatchResult

class SingleRunnerSpec extends Specification {
  private val config = new PettswoodConfig(
    sourceRoot = "src/test/resources/pettswoodStuff",
    mixinPackages = Seq(""))

  "I can haz test" in pettswood(s"${config.sourceRoot}/Pettswood.html")
  "I can haz test" in pettswood(s"${config.sourceRoot}/PoorlyFormedXml.html")

  def pettswood(path: String): MatchResult[Boolean] = SingleRunner(path, config).get.overallPass must beTrue
}