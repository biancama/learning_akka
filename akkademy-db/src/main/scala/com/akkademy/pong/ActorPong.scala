package com.akkademy.pong

import akka.actor.Actor

/**
  * Created by massimo.biancalani on 2019-04-25.
  *
  */
class ActorPong extends Actor {
  override def receive = {
    case "ping" => sender() ! "pong"
    case _ => throw new Exception("unknown command")
  }
}
