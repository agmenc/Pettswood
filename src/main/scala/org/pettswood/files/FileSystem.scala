package org.pettswood.files

import scala.util.Properties._
import org.pettswood.parsers.HtmlToXml
import FileSystem._
import scala.io.Source._
import java.net.URL
import java.io._

class FileSystem {
  def loadResource(resourcePath: String) = fromInputStream(url(resourcePath).openStream()).mkString
  def loadXml(filePath: String) = HtmlToXml(fromFile(filePath).mkString)
  def save(data: String) = Saver(data)
  def in(path: String) = Finder(absolute(path))
  def copy(source: String, destination: String) { save(fromFile(source).mkString) to destination }
  def url(resourcePath: String): URL = getClass.getClassLoader.getResource(resourcePath)

  def firstLine(filePath: String): String = {
    val reader = new BufferedReader(new FileReader(filePath))
    val firstLine = reader.readLine()
    reader.close()
    firstLine
  }
}

object FileSystem {
  def absolute(path: String) = userDir + File.separator + path
}

case class Finder(path: String) {
  def filterFor(fileNamePattern: String): FileFilter = {
    (file: File) => file.isDirectory || fileNamePattern.r.findAllIn(file.getName).hasNext
  }

  def find(fileNamePattern: String): List[String] = {
    find(new File(path), filterFor(fileNamePattern)).map(_.getPath)
  }

  def find(dir: File, filter: FileFilter): List[File] = {
    val files = dir.listFiles(filter)
    val fileSeq = if (files == null) Seq.empty[File] else files.toSeq
    fileSeq.flatMap({
      case file: File if file.isDirectory => find(file, filter)
      case file: File => List(file)
    }).toList
  }
}

case class Saver(data: String) {
  def to(path: String) {
    val writer = new PrintWriter(guarantee(path))
    writer.write(data)
    writer.close()
  }

  def guarantee(path: String): File = {
    val file = new File(path)
    file.getParentFile.mkdirs()
    file.createNewFile()
    file
  }
}