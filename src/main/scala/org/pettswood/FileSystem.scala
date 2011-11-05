package org.pettswood

import xml.XML
import java.io.{FileFilter, File, PrintWriter}

class FileSystem {
  def load(filePath: String) = XML.loadFile(filePath)

  def save(data: String): Saver = Saver(data)

  def in(path: String): Finder = Finder(path)
}

case class Finder(path: String) {
  def filterFor(fileNamePattern: String): FileFilter = {
    new FileFilter {
      def accept(file: File) = file.isDirectory || fileNamePattern.r.findAllIn(file.getName).hasNext
    }
  }

  def find(fileNamePattern: String): List[String] = {
    find(new File(path), filterFor(fileNamePattern)).map(_.getPath)
  }

  def find(dir: File, filter: FileFilter): List[File] = {
    dir.listFiles(filter).flatMap({
      case file: File if (file.isDirectory) => find(file, filter)
      case file: File => List(file)
    }).toList
  }
}

case class Saver(data: String) {
  def to(path: String) {
    val writer = new PrintWriter(guarantee(path))
    writer.write(data)
    writer.close();
  }

  def guarantee(path: String): File = {
    val file = new File(path)
    file.getParentFile.mkdirs()
    file.createNewFile()
    file
  }
}