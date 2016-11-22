package org.pettswood.files

import org.pettswood.PettswoodConfig

class PettswoodFiles(overrideConfig: Option[PettswoodConfig] = None) {
  val fileSystem = new FileSystem
  def sourceRoot = overrideConfig.getOrElse(PettswoodConfig.current).sourceRoot
  def testFilePaths: Seq[String] = fileSystem in sourceRoot find ".*.html"
}