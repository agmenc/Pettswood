package org.pettswood

import files.FileSystem
import org.specs2.mutable.Specification
import org.specs2.mock._
import parsers.xml.scala.Parser
import runners.DisposableRunner

class EndToEndSpec extends Specification with Mockito {
  "we can run a single test" in {
    val config = new PettswoodConfig(mixinPackages = Seq(""))
    val domain = new DomainBridge(config.mixinPackages)

    new DisposableRunner(new Parser(domain), new FileSystem, config).run("src/test/resources/pettswoodStuff/WritingTestsAndFixture.html")

    domain.summary.overallPass must be equalTo true
  }
}