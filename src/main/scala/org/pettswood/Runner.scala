package org.pettswood

import scala.xml.Node

class Runner(parser: Parser, fileSystem: FileSystem) {

  def run(inputPath: String) {
    prepareDirectories()
    val rawResult = execute(load(inputPath))
    val result = parser.decorate(rawResult)
    write(result, outputPath(inputPath))
  }

  def load(inputPath: String): Node = fileSystem loadXml inputPath
  def execute(test: Node): Node = parser.parse(test)
  def write(result: Node, path: String) { fileSystem save result.toString() to path}
  def outputPath(path: String) = path replace("src/test/resources", "target/pettswood")

  def prepareDirectories() {
    ifNoCssIn("src/test/resources") { fileSystem.save(fileSystem.loadFromClasspath("pettswood.css")) to ("src/test/resources/pettswood.css") }
    ifNoCssIn("target/pettswood") { fileSystem.copy("src/test/resources/pettswood.css", "target/pettswood/pettswood.css") }
  }

  def ifNoCssIn(path: String)(f: => Unit) {
    fileSystem in (path) find ("pettswood.css") match {
      case Nil => f
      case alreadyCopied =>
    }
  }
}