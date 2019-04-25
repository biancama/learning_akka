package com.akkademy

import java.lang.String

import akka.actor.{Actor, ActorLogging}

/**
  * Created by massimo.biancalani on 2019-04-24.
  *
  */
class LastMessageActor extends Actor with ActorLogging {
  var lastMessage = ""

  override def receive = {
    case s: String =>
      lastMessage = s

  }
}
