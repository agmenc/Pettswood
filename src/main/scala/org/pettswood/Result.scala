package org.pettswood

abstract class Result(actualText: String) {
  def text = actualText
  def name = this.getClass.getSimpleName
}

object Result {
  def resultFor(expectedText: String, actualText: String) = if (expectedText == actualText) Pass(actualText) else Fail(actualText)
}

case class Fail(actualText: String) extends Result(actualText)
case class Pass(actualText: String) extends Result(actualText)
case class Setup() extends Result("")
case class Exception(exception: String) extends Result(exception)
