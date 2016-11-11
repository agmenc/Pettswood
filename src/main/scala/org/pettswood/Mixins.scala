package org.pettswood

import java.lang.reflect.InvocationTargetException

class Mixins(domain: DomainBridge, mixinPackages: Seq[String]) extends Concept {

  def cell(mixinClassName: String) = {
    val possibleCanonicals = mixinPackages.map(packagePrefix(_) + mixinClassName)
    instantiate(possibleCanonicals, mixinClassName) match {
      case Some(c: Concept) => domain.learn(mixinClassName, () => instanceOf(mixinClassName).asInstanceOf[Concept])
      case Some(m: Mixin) =>
      case _ => throw new MixinException(mixinPackages, mixinClassName)
    }

    Setup()
  }

  def packagePrefix(packidge: String) = if (packidge.trim() == "") "" else packidge.trim + "."

  def instantiate(possibleCanonicals: Seq[String], className: String): Option[Any] = {
    try {
      Some(instanceOf(possibleCanonicals.head))
    } catch {
      case c: ClassNotFoundException => if(possibleCanonicals.size > 1) instantiate(possibleCanonicals.tail, className) else None
      case i: InvocationTargetException => throw unwrapped(i)
    }
  }

  def unwrapped(i: InvocationTargetException) = if (i.getCause == null) i else i.getCause

  def instanceOf(classWithDefaultConstructor: String) = {
    val clazz = Class.forName(classWithDefaultConstructor)
    val firstConstructor = clazz.getDeclaredConstructors.head
    if (new Mixin(domain).getClass.isAssignableFrom(clazz))
      firstConstructor.newInstance(domain)
    else
      firstConstructor.newInstance()
  }
}

class MixinException(mixinPackages: Seq[String], className: String) extends RuntimeException("Could not find " + className + " in packages [" + mixinPackages.mkString(", ") + "]")