package org.pettswood

import org.pettswood.runners.sbt.SbtIntegrationHook

/**
  * This is an example of how to run Pettswood in your sbt build. First: extend SbtIntegrationHook. Second, override
  * PettswoodConfig.current if you wish to change the default settings (test file locations, source roots, etc)
  */
class PettswoodSbt extends SbtIntegrationHook {
  PettswoodConfig.current = PettswoodConfig(cssAndJsDirectoriesToCopy = Seq("not", "actually", "the", "correct", "directories"))
}