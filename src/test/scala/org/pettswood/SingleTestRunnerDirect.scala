package org.pettswood

import runners.SingleRunner

object SingleTestRunnerDirect extends App {
  private val config = PettswoodConfig(
    sourceRoot = "src/test/resources/pettswoodStuff",
    mixinPackages = Seq(""))

  SingleRunner("src/test/resources/pettswoodStuff/StatePassedBetweenConcepts.html", config)
}