package org.pettswood.runners

import scala.xml.Node
import org.pettswood.parsers.xml.scala.Parser
import org.pettswood.{PettswoodConfig, ResultSummary, DomainBridge}
import org.pettswood.files.FileSystem

class DisposableRunner(parser: Parser, fileSystem: FileSystem, config: PettswoodConfig) {

  def run(inputPath: String): ResultSummary =  {
    copyResourcesToTest()
    copyResourcesToTarget()
    val rawResult = parser.parse(load(inputPath))
    val decoratedResult = parser.decorate(rawResult)
    write(decoratedResult, outputPath(inputPath))
    parser.summary
  }

  def load(inputPath: String): Node = fileSystem loadXml inputPath
  def write(result: Node, path: String) { fileSystem save result.toString() to path}
  def outputPath(path: String) = path replaceAll("src.*resources", "target/pettswood")

  def copyResourcesToTest() {
    ifNotFound("src/test/resources/css", ".*.css") { fileSystem.save(fileSystem.loadResource("css/pettswood.css")) to "src/test/resources/css/pettswood.css" }
    ifNotFound("src/test/resources/javascript", ".*.js") {
      fileSystem.save(fileSystem.loadResource("javascript/pettswood.js")) to "src/test/resources/javascript/pettswood.js"
      fileSystem.save(fileSystem.loadResource("javascript/jquery-1.7.2.min.js")) to "src/test/resources/javascript/jquery-1.7.2.min.js"
    }
  }

  def copyResourcesToTarget() {
    fileSystem in "src/test/resources/css" find ".*.css" foreach (path => fileSystem.copy(path, outputPath(path)))
    fileSystem in "src/test/resources/javascript" find ".*.js" foreach (path => fileSystem.copy(path, outputPath(path)))
    fileSystem in "src/test/resources" find "bootstrap" foreach (path => fileSystem.copy(path, outputPath(path)))
  }

  def ifNotFound(path: String, filenamePattern: String)(f: => Unit) {
    fileSystem in path find filenamePattern match {
      case Nil => f
      case alreadyCopied =>
    }
  }
}

class RecycleableRunner(config: PettswoodConfig) {
  def run(filePath: String) = {
    val domainBridge = new DomainBridge(config.mixinPackages)
    new DisposableRunner(new Parser(domainBridge), new FileSystem, config).run(filePath)
  }
}

object SingleRunner {
  def apply(inputPath: String, config: PettswoodConfig) = run(inputPath, config)

  private def run(inputPath: String, config: PettswoodConfig) = {
    val summary = new RecycleableRunner(config).run(inputPath)
    println(" " + summary.toString + " ==> " + inputPath)
    Some(summary)
  }
}