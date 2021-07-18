package com.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}

trait FutureOptionExtensions {
  implicit class RichFutureOption[T](extendee: Future[Option[T]]) {
    def dumap[T1](mapper: T => Future[Option[T1]])(implicit executor: ExecutionContext): Future[Option[T1]] =
      extendee.flatMap {
        case Some(value) => mapper(value)
        case _ => Future.successful(None)
      }
  }

  implicit class RichOptionFuture[T](extendee: Option[Future[T]]) {
    def dummed(implicit executor: ExecutionContext): Future[Option[T]] =
      extendee match {
        case Some(r) => r.map(Some(_))
        case _ => Future.successful(None)
      }
  }

}
