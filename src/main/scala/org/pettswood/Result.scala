package org.pettswood

trait Result {
  def text:String
  def name = this.getClass.getSimpleName
}

object Result {
  def given(expectedText: String, actualText: String) = if (expectedText == actualText) Pass(actualText) else Fail(actualText)
}

case class Fail(override val text: String) extends Result
case class Pass(override val text: String) extends Result
case class Setup() extends Result { override val text = "" }
case class Description() extends Result { override val text = "" }
case class Uninteresting() extends Result { override val text = "" }

case class Exception(exception: Throwable) extends Result {
  override def text = exception.getMessage
  override def equals(that: Any) = that match {
    case Exception(e) => e.toString == exception.toString
    case anyOther => false
  }
}
