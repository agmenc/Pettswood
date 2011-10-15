package org.pettswood

import util.Properties._
import java.io.File
import xml.Node

class Runner(parser: Parser, fileSystem: FileSystem) {
  def run(inputPath: String) { write(execute(load(inputPath))) }
  def load(inputPath: String): Node = fileSystem load absolute(inputPath)
  def execute(test: Node): Node = parser.parse(test)
  def write(result: Node) { fileSystem save result.toString() to "target/pettswood/Overworked Example.html"}
  def absolute(path: String) = userDir + File.separator + path
}
