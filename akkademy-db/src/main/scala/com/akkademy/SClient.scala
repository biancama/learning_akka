package com.akkademy

import akka.actor.ActorSystem
import akka.util.Timeout
import com.akkademy.messages.{Delete, GetRequest, SetIfNotExists, SetRequest}
import akka.pattern.ask
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
/**
  * Created by massimo.biancalani on 2019-04-26.
  *
  */
class SClient(remoteAddress: String) {
  private val root = ConfigFactory.load
  private val client = root.getConfig("client")

  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem", client)

  private val remoteDb = system.actorSelection(s"akka.tcp://akkademy@${remoteAddress}/user/akkademy-db")

  def set (key:String, value: Object) = remoteDb ? SetRequest(key, value)

  def get(key: String) = remoteDb ? GetRequest(key)

  def setIfNotExist(key: String, value: Object) = remoteDb ? SetIfNotExists(key, value)

  def delete (key: String) = remoteDb ? Delete(key)
}
