package com.akkademy.reverse

import akka.actor.Actor
import akka.actor.Status.{Failure, Success}

/**
  * Created by massimo.biancalani on 2019-04-26.
  *
  */
class ReverseActor extends Actor {
  override def receive = {
    case s:String => Success(sender() ! s.reverse)
    case _  => Failure(new IllegalArgumentException("Accept only string"))
   }
}
