package com.github.dumonad.dumonad

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.github.dumonad.dumonad.Implicits._

class Main {
  def main(args: Array[String]): Unit = {
    def firstCall: Future[Either[String, String]] = Future.successful(Right("PING"))

    def secondCall(param: String): Future[Either[String, String]] = Future.successful(Right(param + param))

    val result: Future[Either[String, String]] = firstCall.flatMap {
      case Right(value) => secondCall(value)
      case left => Future.successful(left)
    }

    firstCall.dumap(secondCall)

    val optionOfFuture: Option[Future[String]] = Some(Future.successful("PING"))

    val futureOfOption: Future[Option[String]] = optionOfFuture match {
      case Some(future) => future.map(Some(_))
      case _ => Future.successful(None)
    }

    optionOfFuture.dummed
  }
}
