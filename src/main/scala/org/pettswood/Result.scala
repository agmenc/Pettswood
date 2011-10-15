package org.pettswood

abstract class Result(expectedText: String, actualText: String) {
  def text = actualText
  def name = this.getClass.getSimpleName
}

case class Fail(expectedText: String, actualText: String) extends Result(expectedText, actualText)
case class Pass(expectedText: String) extends Result(expectedText, expectedText)
case class Setup() extends Result("", "")
case class Exception(expectedText: String, exceptionTrace: String) extends Result(expectedText, exceptionTrace)
