package com.akkademy

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}

/**
  * Created by massimo.biancalani on 2019-04-24.
  *
  */
class LastMessageActorSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {
  implicit val system = ActorSystem()

  describe("LastMessageActor") {
    describe("given a string") {
      it("should save in the last message") {
        val lastMessageActor = TestActorRef( new LastMessageActor)
        lastMessageActor ! "message"

        lastMessageActor.underlyingActor.lastMessage should equal("message")
      }
    }

    describe("given 2 strings") {
      it ("the second string should override the first one") {
        val lastMessageActor = TestActorRef(new LastMessageActor)
        lastMessageActor ! "first message"
        lastMessageActor ! "second message"

        lastMessageActor.underlyingActor.lastMessage should equal("second message")

      }
    }
  }
}
