package org.pettswood

import scala.collection.mutable
import scala.reflect.ClassTag

class State {
  val stateMap = mutable.Map[Class[_], Any]()

  def retrieve[T : ClassTag](default: () => T) = {
    val key = implicitly[ClassTag[T]].runtimeClass
    if (!stateMap.contains(key)) stateMap += key -> default()
    stateMap(key).asInstanceOf[T]
  }
}
