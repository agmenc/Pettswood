package org

package object pettswood {
  def doThis(f: (String) => Unit) = (text: String) => new Doer {f(text)}

  def dig(f: (String) => String) = (text: String) => new Digger {
    def actual = f(text)
  }

  // TODO - CAS - 26/04/2013 - Convert this to Probe.and(), e.g.:
  // doThis() and doThis() and dig()
  def doAll(doers: (String => Doer)*) = (text: String) => new Doer {
    doers.foreach(_(text))
  }
}
