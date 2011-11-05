package org.pettswood

import util.Properties._
import xml.Node
import java.io.File

class Runner(parser: Parser, fileSystem: FileSystem) {
  def run(inputPath: String) { write(execute(load(inputPath)), outputPath(inputPath)) }
  def load(inputPath: String): Node = fileSystem load absolute(inputPath)
  def execute(test: Node): Node = parser.parse(test)
  def write(result: Node, path: String) { fileSystem save result.toString() to path}
  def absolute(path: String) = userDir + File.separator + path
  def outputPath(path: String) = absolute(path replace("src/test/resources", "target/pettswood"))
}

object Runner extends App {
  // Copy CSS and JS from home dir to target dir
  for (filePath <- (new FileSystem) in "src/test/resources" find ".*.html") new Runner(new Parser(new DomainBridge), new FileSystem).run(filePath)
}