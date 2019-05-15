package com.akkademy.askdemo

import akka.actor.Status.Failure
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import com.akkademy.messages.{GetRequest, SetRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.TimeoutException
/**
  * Created by massimo.biancalani on 2019-05-09.
  *
  */
class TellDemoArticleParser(cacheActorPath: String, httpClientActorPath: String, articleParserActorPath: String, implicit val timeout: Timeout) extends Actor {
  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  private def buildExtraActor(ref: ActorRef, uri: String) : ActorRef = {
    return context.actorOf(Props(new Actor {
      override def receive = {
        case "timeout" => // if we get timeout, then fail
          ref ! Failure(new TimeoutException("timeout!"))
          context stop(self)

        case HttpResponse(body) => // if we get http response, then we pass it to be parsed
          articleParserActor ! ParseHtmlArticle(uri, body)

        case ArticleBody(url, body) => // if we get Article, then it should be add to cache
          cacheActor ! SetRequest(url, body)
          ref ! body
          context stop(self)

        case t =>
          println("ignoring message: " + t.getClass)
      }
    }))
  }

  override def receive: Receive = {
    case ParseArticle(uri) =>
      val extraActor = buildExtraActor(sender(), uri)

      cacheActor ! (GetRequest(uri), extraActor)

      httpClientActor ! (uri, extraActor)

      context.system.scheduler.scheduleOnce(timeout.duration, extraActor, "timeout")
  }
}
