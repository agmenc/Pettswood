package org.pettswood

class Mixins(domain: DomainBridge) extends Concept {

  def cell(className: String) = {
    try {
      // TODO - collapse with a handleWith(handler) { ... }
      val instance = instanceOf(className)
      if (instance.isInstanceOf[Concept]) domain.learn(className, () => instanceOf(className).asInstanceOf[Concept])
      Setup()
    } catch {
      case e => Exception(className, e getMessage)
    }
  }

  def instanceOf(classWithDefaultConstructor: String) = {
    val clazz = Class.forName(classWithDefaultConstructor)
    val firstConstructor = clazz.getDeclaredConstructors.head
    if (new Grouper(domain).getClass.isAssignableFrom(clazz))
      firstConstructor.newInstance(domain)
    else
      firstConstructor.newInstance()
  }
}