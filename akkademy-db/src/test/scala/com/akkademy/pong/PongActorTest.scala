package com.akkademy.pong

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by massimo.biancalani on 2019-04-25.
  *
  */
class PongActorTest extends FunSpecLike with Matchers {
  val system = ActorSystem()

  implicit val timeout = Timeout(5 seconds)

  val pongActor = system.actorOf(Props(classOf[ActorPong]))

  describe("pong actor") {
    it("should respond with pong") {
      val future = pongActor ? "ping"  // implicit timeout
      val result = Await.result(future.mapTo[String], 1 second)
      assert(result == "pong")
    }
    it("should fail for unknow message") {
      val future = pongActor ? "unknown"  // implicit timeout
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }
}
