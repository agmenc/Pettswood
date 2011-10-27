package org.pettswood.specification.concepts

class Calculator {
  def calculate(inputs: List[String]) = {
    inputs(1) match {
      case "+" => (inputs(2).toInt + inputs(0).toInt) + ""
      case "-" => (inputs(2).toInt - inputs(0).toInt) + ""
      case _ => throw new RuntimeException("Unsupported mathematical operation: " + inputs(1))
    }
  }
}