package org

package object pettswood {
  def doThis(f: (String) => Unit) = (text: String) => new Doer {f(text)}
  def dig(f: (String) => String) = (text: String) => new Digger {
    def actual = f(text)
  }
}
