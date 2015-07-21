package org.pettswood

case class PettswoodConfig(mixinPackages: Seq[String] = Seq("pettswood"),
                           sourceRoot: String = "src/test/resources/pettswood",
                           cssAndJsDirectoriesToCopy: Seq[String] = Seq("css", "javascript", "bootstrap"))

object PettswoodConfig {
  var current = new PettswoodConfig()
}