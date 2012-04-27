package org.pettswood.specification.concepts

import org.pettswood.{Result, Concept}

class HelloWorld extends Concept {
  protected def cell(text: String) = Result.given("world", text)
}

