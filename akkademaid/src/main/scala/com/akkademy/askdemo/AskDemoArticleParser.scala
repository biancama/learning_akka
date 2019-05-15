package com.akkademy.askdemo

import akka.actor.Actor
import akka.util.Timeout
import akka.pattern.ask
import com.akkademy.messages.{GetRequest, SetRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class AskDemoArticleParser(cacheActorPath: String, httpClientActorPath: String, articleParserActorPath: String, implicit val timeout: Timeout) extends Actor {
  val cacheActor = context.actorSelection(cacheActorPath)
  val httpClientActor = context.actorSelection(httpClientActorPath)
  val articleParserActor = context.actorSelection(articleParserActorPath)

  override def receive: Receive = {

    case ParseArticle(path) => {
      val senderRef = sender()
      val cacheResult = cacheActor ? GetRequest(path)
      val result = cacheResult.recoverWith {
        case _: Exception => {
          val rawArticleFuture = httpClientActor ? GetRequest(path)
          rawArticleFuture flatMap {
            case HttpResponse(rawArticle) => articleParserActor ? ParseHtmlArticle(path, rawArticle)
            case x => Future.failed(new Exception("Unknown message"))
          }
        }
      }
      result onComplete {
        case Success(article: String) => {
          println("Cached result!")
          senderRef ! article
        }
        case Success(art: ArticleBody) => {
          cacheActor ! SetRequest(path, art)
          senderRef ! art
        }
        case Failure(e) => senderRef ! Failure(e)
        case x => println("Unknown message!")

      }
    }
  }

}
