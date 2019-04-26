package com.akkademy

import akka.actor.Actor
import akka.actor.Status.{Failure, Success}
import akka.event.Logging
import com.akkademy.messages.{Delete, GetRequest, KeyNotFoundException, SetIfNotExists, SetRequest}

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
      sender() ! Success
    }
    case GetRequest(key) => {
      log.info(" Received GetRequest - key {}", key)
      map.get(key) match {
        case Some(value) => sender ! value
        case None => sender() ! Failure(new KeyNotFoundException(s"the key: ${key} not found in the db"))
      }
    }

    case SetIfNotExists(key, value) => {
      log.info("Received SetIfNotRequest - key : {} value: {}", key, value)
      map.get(key) match {
        case Some(s) => sender ! Success
        case None => {
            map.put(key, value)
            sender ! Success
          }
      }
    }

    case Delete (key) => {
      log.info ("Delete key: {}", key)
      map.remove(key) match {
        case Some(s) => {
          sender ! Success
        }
        case None => {
          sender ! Failure(new KeyNotFoundException(s"the key: ${key} not found in the db"))
        }
      }
    }
    case o => {
      log.info("Received unknown message : {} value: {}", o)
      sender ! Failure ( new ClassNotFoundException)
    }
  }

}
