package org.pettswood

import xml.XML
import java.io.{File, PrintWriter}

class FileSystem {
  def load(filePath: String) = XML.loadFile(filePath)

  def save(data: String): Saver = Saver(data)

  def in(path: String): Finder = Finder(path)
}

case class Finder(path: String) {
  def find(fileNamePattern: String): List[String] = {
    List.empty[String]
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