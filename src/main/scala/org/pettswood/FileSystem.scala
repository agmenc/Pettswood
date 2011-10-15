package org.pettswood

import xml.XML
import java.io.{File, PrintWriter}

class FileSystem {
  def load(filePath: String) = XML.loadFile(filePath)

  def save(data: String): Saver = Saver(data)
}

case class Saver(data: String) {
  def to(path: String) {
    val writer = new PrintWriter(guarantee(path))
    writer.write(data)
    writer.close();
  }

  def guarantee(path: String): File = {
    // split the path into dirs
    // foreach dir, iff it doesn't exist, create it
    new File(path)
  }
}

object FileSystem extends FileSystem