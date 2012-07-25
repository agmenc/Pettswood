package org.pettswood.runners

import scala.xml.Node
import org.pettswood.parsers.xml.scala.Parser
import org.pettswood.{ResultSummary, DomainBridge}
import org.pettswood.files.FileSystem

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
  def outputPath(path: String) = path replaceAll("src.*resources", "target/pettswood")

  def prepareDirectories() {
    ifNotFound("src/test/resources/css", ".*.css") { fileSystem.save(fileSystem.loadResource("css/pettswood.css")) to ("src/test/resources/css/pettswood.css") }
    ifNotFound("src/test/resources/javascript", ".*.js") {
      fileSystem.save(fileSystem.loadResource("javascript/pettswood.js")) to ("src/test/resources/javascript/pettswood.js")
      fileSystem.save(fileSystem.loadResource("javascript/jquery-1.7.2.min.js")) to ("src/test/resources/javascript/jquery-1.7.2.min.js")
    }
    fileSystem in ("src/test/resources/css") find (".*.css") foreach (path => fileSystem.copy(path, outputPath(path)))
    fileSystem in ("src/test/resources/javascript") find (".*.js") foreach (path => fileSystem.copy(path, outputPath(path)))
  }

  def ifNotFound(path: String, filenamePattern: String)(f: => Unit) {
    fileSystem in (path) find (filenamePattern) match {
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
  def apply(inputPath: String) = run(inputPath)

  def fromSystemProperty() = sys.props.get("input.path") match {
    case Some(inputPath) => run(inputPath)
    case None => println("\nPlease set the system property \"input.path\'\n")
  }

  private def run(inputPath: String) = {
    val summary = DefaultRunner.run(inputPath)
    println(" " + summary.toString + " ==> " + inputPath)
    Some(summary)
  }
}