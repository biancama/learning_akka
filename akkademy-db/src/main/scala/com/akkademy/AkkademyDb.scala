package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import com.akkademy.messages.SetRequest

import scala.collection.mutable.HashMap

/**
  * Created by massimo.biancalani on 2019-04-24.
  *
  */
class AkkademyDb extends Actor {
  val map = new HashMap[String, Object]
  val log = Logging(context.system, this)

  override def receive=  {
    case SetRequest(key, value) => {
      log.info("Received SetRequest - key : {} value: {}", key, value)
      map.put(key, value)
    }
    case o => log.info("Received unknown message : {} value: {}", o)
  }

}
