package org.pettswood

import runners.SingleRunner

object SingleTestRunnerDirect extends App {
  SingleRunner("src/test/resources/AdvancedTopics.html")
}

object SingleTestRunnerSysProp extends App {
  SingleRunner.fromSystemProperty()
}
