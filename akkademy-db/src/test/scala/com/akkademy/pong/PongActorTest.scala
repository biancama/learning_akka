package com.akkademy.pong

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import akka.testkit.TestActor.Message
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
/**
  * Created by massimo.biancalani on 2019-04-25.
  *
  */
class PongActorTest extends FunSpecLike with Matchers {
  val system = ActorSystem()

  implicit val timeout = Timeout(5 seconds)

  val pongActor = system.actorOf(Props(classOf[ActorPong]))

  def askPong (message: String): Future[String] = (pongActor ? message).mapTo[String]

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
    it ("should print to console") {
      askPong("ping").onComplete {
        case Success(answer) => println("replied with: " + answer)
        case Failure(t) => println("An error has occurred: " + t.getMessage)
      }
    }

    it ("should transform the result") {
      askPong("ping").map(x => x.charAt(0)) onComplete {
        case Success(s) => println (s"Mapped with ${s}")
        case Failure(t) => println("An error has occurred: " + t.getMessage)
      }
    }

    
  }
}
