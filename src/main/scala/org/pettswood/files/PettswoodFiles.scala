package org.pettswood.files

import org.pettswood.PettswoodConfig._

trait PettswoodFiles {
  def testFilePaths: Seq[String]
  def resourcePaths: Seq[String]
}

object DefaultPettswoodFiles extends PettswoodFiles {
  val fileSystem = new FileSystem
  def testFilePaths = testSources.flatMap(fileSystem in _ find ".*.html")
  def resourcePaths = Seq()
}