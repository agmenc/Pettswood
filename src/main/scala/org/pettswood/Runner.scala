package org.pettswood

import util.Properties._
import java.io.File
import xml.Node

class Runner(parser: Parser, fileSystem: FileSystem) {
  def run(inputPath: String) { write(execute(load(inputPath)), outputPath(inputPath)) }
  def load(inputPath: String): Node = fileSystem load absolute(inputPath)
  def execute(test: Node): Node = parser.parse(test)
  def write(result: Node, path: String) { fileSystem save result.toString() to path}
  def absolute(path: String) = userDir + File.separator + path
  def outputPath(path: String) = absolute(path replace("src/test/resources", "target/pettswood"))
}

object Runner {
  // Find all test files
  // Create & run runners for 'em (start consecutively first)
  // Copy CSS and JS from home dir to target dir
}