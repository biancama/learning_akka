package com.akkademy

import com.akkademy.messages.KeyNotFoundException
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by massimo.biancalani on 2019-04-26.
  *
  */
class SClientSpec extends FunSpecLike with Matchers with BeforeAndAfterEach {

  val client = new SClient("127.0.0.1:2552")
  describe("AkkademyDbClient") {
    it ("should set a value") {
      client.set("123", Integer.valueOf(123))
      val futureResult = client.get("123")
      val result = Await.result(futureResult, 10 seconds)
      result should equal(123)
    }

    it ("should replace a value if already exists") {
      client.set("first", Integer.valueOf(123))
      client.set("first", "this is a string")
      val futureResult = client.get("first")
      val result = Await.result(futureResult, 10 seconds)
      result should equal("this is a string")

    }

    it ("should delete a value only if exists") {

      client.set("second", Integer.valueOf(123))
      val futureResult = client.get("second")
      val result = Await.result(futureResult, 10 seconds)
      result should equal(123)


      client.delete("second")

      intercept[KeyNotFoundException] {
        val futureResultAfterDelete = client.get("second")
        Await.result(futureResultAfterDelete, 10 seconds)
      }

      intercept[KeyNotFoundException] {
        val futureTryDeleteNotExisting = client.delete("NOT_EXIST")
        Await.result(futureTryDeleteNotExisting, 10 seconds)
      }

    }
  }

}
