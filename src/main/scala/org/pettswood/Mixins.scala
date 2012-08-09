package org.pettswood

class Mixins(domain: DomainBridge) extends Concept {

  def cell(className: String) = {
    val possibleCanonicals = PettswoodConfig.mixinPackages.map(packagePrefix(_) + className)
    instantiate(possibleCanonicals, className) match {
      case Some(c: Concept) => domain.learn(className, () => instanceOf(className).asInstanceOf[Concept])
      case Some(m: Mixin) =>
      case None => throw new MixinException(PettswoodConfig.mixinPackages, className)
    }

    Setup()
  }

  def packagePrefix(packidge: String) = if (packidge.trim() == "") "" else packidge.trim + "."

  def instantiate(possibleCanonicals: Seq[String], className: String): Option[Any] = {
    try {
      Some(instanceOf(possibleCanonicals.head))
    } catch {
      case t: ClassNotFoundException => if(possibleCanonicals.size > 1) instantiate(possibleCanonicals.tail, className) else None
    }
  }

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