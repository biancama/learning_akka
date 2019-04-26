package com.akkademy

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * Created by massimo.biancalani on 2019-04-26.
  *
  */
object Main extends App {
  private val root = ConfigFactory.load
  private val server = root.getConfig("server")
  val system = ActorSystem("akkademy", server)

  system.actorOf(Props[AkkademyDb], name ="akkademy-db")
}
