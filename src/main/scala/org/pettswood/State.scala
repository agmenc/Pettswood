package org.pettswood

import scala.collection.mutable
import scala.reflect.{ClassTag, classTag}

class State {
  val stateMap = mutable.Map[Class[_], Any]()
  val classOfNothing = classOf[scala.runtime.Nothing$]

  def retrieve[T : ClassTag](default: () => T) = {
    val key = tag()
    if (!stateMap.contains(key)) stateMap += key -> default()
    stateMap(tag()).asInstanceOf[T]
  }

  def retrieve[T : ClassTag](): Option[T] = {
    val key: Class[_] = tag[T]()

    if (key.isAssignableFrom(classOfNothing)) throw new MissingTypeParameterException(classOf[State], "retrieve")

    if (stateMap.contains(key))
      Some(stateMap(key).asInstanceOf[T])
    else
      None
  }

  def tag[T: ClassTag](): Class[_] = classTag[T].runtimeClass
}

class MissingTypeParameterException(clazz: Class[_], methodName: String)
  extends IllegalArgumentException(s"Missing type parameter in call to ${clazz.getCanonicalName}.$methodName")
