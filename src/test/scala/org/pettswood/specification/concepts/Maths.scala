package org.pettswood.specification.concepts

import org.pettswood.{Result, Pass, Concept}

// TODO - lots of this belongs in MultiRowConcept
class Maths extends Concept {

  var rowPointer, columnPointer = 0;

  override def row() {rowPointer += 1}

  def cell(text: String) = rowPointer match {
    case 1 => Pass(text)
    case 2 => mapToProbe(text)
    case _ => probe(text)
  }

  def mapToProbe(text: String): Result = Pass(text)
  def probe(text: String): Result = Pass(text)
}