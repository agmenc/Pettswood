package org.pettswood

abstract class Result(actualText: String) {
  def text = actualText
  def name = this.getClass.getSimpleName
}

object Result {
  def given(expectedText: String, actualText: String) = if (expectedText == actualText) Pass(actualText) else Fail(actualText)
}

case class Fail(actualText: String) extends Result(actualText)
case class Pass(actualText: String) extends Result(actualText)
case class Setup() extends Result("")
case class Exception(exception: Throwable) extends Result(exception.getMessage) {
  override def equals(that: Any) = that match {
    case Exception(e) => e.toString == exception.toString
    case anyOther => false
  }
}
