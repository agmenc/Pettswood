package org.pettswood

abstract class Result(expectedText: String, actualText: String) {
  def text = actualText
}

case class Wrong(expectedText: String, actualText: String) extends Result(expectedText, actualText)
case class Right(expectedText: String) extends Result(expectedText, expectedText)
case class Setup() extends Result("", "")
case class Exception(expectedText: String, exceptionTrace: String) extends Result(expectedText, exceptionTrace)
