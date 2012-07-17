package org.pettswood

import org.pettswood.runners.sbt.SbtIntegrationHook

class PettswoodSbt extends SbtIntegrationHook {
  PettswoodConfig.testSources = Seq("src/test/resources")
}