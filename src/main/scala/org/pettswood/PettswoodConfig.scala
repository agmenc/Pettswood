package org.pettswood

/**
  *
  * @param mixinPackages JVM packages (e.g. org.mything.mixins) containing Mixin classes
  * @param sourceRoot The source directory for the HTML tests that are executed by PettsWood
  * @param cssAndJsDirectoriesToCopy
  */
case class PettswoodConfig(mixinPackages: Seq[String] = Seq("", "pettswood"),
                           sourceRoot: String = "src/test/resources",
                           cssAndJsDirectoriesToCopy: Seq[String] = Seq("css", "javascript", "bootstrap"))

object PettswoodConfig {
  var current = new PettswoodConfig()
}