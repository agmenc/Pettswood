package org.pettswood.specification.concepts

import org.pettswood.{Exception, Concept}

class Maths extends Concept {
  def cell(text: String) = Exception("No probe defined for this column/cell", "No probe defined for this column/cell")
}