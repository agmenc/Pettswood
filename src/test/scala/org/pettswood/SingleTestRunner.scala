package org.pettswood

import runners.DefaultRunner

object SingleTestRunner extends App {
  val inputPath = "src/test/resources/AdvancedTopics.html"
  val summary = DefaultRunner.run(inputPath)
  println(" " + summary.toString + " ==> " + inputPath)
}