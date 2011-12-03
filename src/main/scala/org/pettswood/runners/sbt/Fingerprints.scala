package org.pettswood.runners.sbt

object Fingerprints {
  val fp1 = new PettswoodFingerprint {def isModule = false}
  val fp2 = new PettswoodFingerprint {def isModule = true}
}