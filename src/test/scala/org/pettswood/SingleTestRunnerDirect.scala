package org.pettswood

import runners.SingleRunner

object SingleTestRunnerDirect extends App {
  SingleRunner("src/test/resources/NestedTables.html")
}

object SingleTestRunnerSysProp extends App {
  SingleRunner.fromSystemProperty()
}
