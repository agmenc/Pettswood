package org.pettswood

import runners.DefaultRunner

object SingleTestRunner extends App {
  DefaultRunner.run("src/test/resources/AdvancedTopics.html")
}