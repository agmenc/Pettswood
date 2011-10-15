package org.pettswood

class Mixins(domain: DomainBridge) extends Concept {

  def cell(className: String) {
    val instance = instanceOf(className)
    if (instance.isInstanceOf[Concept]) domain.learn(className, instance.asInstanceOf[Concept])
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