package org.pettswood.runners

import org.pettswood.files.FileSystem
import org.pettswood.parsers.xml.scala.Parser
import org.pettswood.{DomainBridge, PettswoodConfig, ResultSummary}

import scala.xml.Node

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
  def outputPath(path: String) = path replaceAll("src.*resources", "target")

  def copyResourcesToTest() {
    ifNotFound(s"${config.sourceRoot}/css", ".*.css") { fileSystem.save(fileSystem.loadResource("css/pettswood.css")) to s"${config.sourceRoot}/css/pettswood.css" }
    ifNotFound(s"${config.sourceRoot}/javascript", ".*.js") {
      fileSystem.save(fileSystem.loadResource("javascript/pettswood.js")) to s"${config.sourceRoot}/javascript/pettswood.js"
      fileSystem.save(fileSystem.loadResource("javascript/jquery-1.7.2.min.js")) to s"${config.sourceRoot}/javascript/jquery-1.7.2.min.js"
    }
  }

  def copyResourcesToTarget() {
    fileSystem in s"${config.sourceRoot}/css" find ".*.css" foreach (path => fileSystem.copy(path, outputPath(path)))
    fileSystem in s"${config.sourceRoot}/javascript" find ".*.js" foreach (path => fileSystem.copy(path, outputPath(path)))
    fileSystem in s"${config.sourceRoot}" find "bootstrap" foreach (path => fileSystem.copy(path, outputPath(path)))
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