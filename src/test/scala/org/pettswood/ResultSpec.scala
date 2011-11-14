package org.pettswood

import org.specs2.mutable.Specification

class ResultSpec extends Specification {
  "Exception results should be equable, even though Java Exceptions try really hard not to be equable," in {
    Exception(new RuntimeException("Sausages")) must be equalTo Exception(new RuntimeException("Sausages"))
  }
}