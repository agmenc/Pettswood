package org.pettswood.files

class PettswoodFiles(sourceRoot: String) {
  val fileSystem = new FileSystem
  def testFilePaths: Seq[String] = fileSystem in sourceRoot find ".*.html"
}