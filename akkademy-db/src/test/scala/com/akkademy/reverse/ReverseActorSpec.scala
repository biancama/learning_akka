package com.akkademy.reverse

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestActorRef
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by massimo.biancalani on 2019-04-26.
  *
  */
class ReverseActorSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {
  val system = ActorSystem()

  describe("ReverseActor") {
    it ("should reverse a string") {
      val reverseActor = TestActorRef.create(system, Props(classOf[ReverseActor]))
      implicit val timeout = Timeout(5 seconds)
      val futureAsked = reverseActor ? "massimo"
      val result = Await.result(futureAsked, 2 seconds)

      result should equal("omissam")

    }

    it("should fail if we send an object NOT a string") {
      val reverseActor = TestActorRef.create(system, Props(classOf[ReverseActor]))
      implicit val timeout = Timeout(5 seconds)

      intercept[Exception]{
        val futureAsked = reverseActor ? Integer.valueOf(123)
        Await.result(futureAsked, 2 seconds)
      }
    }

    it("should return a try") {
      val reverseActor = TestActorRef.create(system, Props(classOf[ReverseActor]))
      implicit val timeout = Timeout(5 seconds)

      reverseActor ? "pippo" onComplete {
        case Success(s) => println(s"Return ${s}")
      }
    }
  }
}
