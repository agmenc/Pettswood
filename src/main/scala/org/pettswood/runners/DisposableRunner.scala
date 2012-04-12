package org.pettswood.runners

import scala.xml.Node
import org.pettswood.parsers.xml.scala.Parser
import org.pettswood.{ResultSummary, FileSystem, DomainBridge}

class DisposableRunner(parser: Parser, fileSystem: FileSystem) {

  def run(inputPath: String): ResultSummary =  {
    prepareDirectories()
    val rawResult = parser.parse(load(inputPath))
    val decoratedResult = parser.decorate(rawResult)
    write(decoratedResult, outputPath(inputPath))
    parser.summary
  }

  def load(inputPath: String): Node = fileSystem loadXml inputPath
  def write(result: Node, path: String) { fileSystem save result.toString() to path}
  def outputPath(path: String) = path replace("src/test/resources", "target/pettswood")

  def prepareDirectories() {
    ifNoCssIn("src/test/resources/css") { fileSystem.save(fileSystem.loadResource("css/pettswood.css")) to ("src/test/resources/css/pettswood.css") }
    fileSystem in ("src/test/resources/css") find (".*.css") foreach (path => fileSystem.copy(path, outputPath(path)))
    fileSystem in ("src/test/resources/javascript") find (".*.js") foreach (path => fileSystem.copy(path, outputPath(path)))
  }

  def ifNoCssIn(path: String)(f: => Unit) {
    fileSystem in (path) find ("pettswood.css") match {
      case Nil => f
      case alreadyCopied =>
    }
  }
}

trait RecycleableRunner {
  def run(filePath: String): ResultSummary
}

object DefaultRunner extends RecycleableRunner {
  def run(filePath: String) = {
    val domainBridge = new DomainBridge
    new DisposableRunner(new Parser(domainBridge), new FileSystem).run(filePath)
  }
}

object SingleRunner {
  def apply(inputPath: String) = {
    val summary = DefaultRunner.run(inputPath)
    println(" " + summary.toString + " ==> " + inputPath)
    summary
  }
}