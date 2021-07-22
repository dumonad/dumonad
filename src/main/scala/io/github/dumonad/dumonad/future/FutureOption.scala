package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}

case class FutureOption[T](value: Future[Option[T]]) {

  def rawFlatMap[T1](mapper: T => Future[Option[T1]])(implicit executor: ExecutionContext): Future[Option[T1]] = {
    value.flatMap {
      case Some(r) => mapper(r)
      case _ => Future.successful(None)
    }
  }

  def flatMap[T1](mapper: T => FutureOption[T1])(implicit executor: ExecutionContext): FutureOption[T1] = {
    val mappedResult = value.flatMap {
      case Some(r) => mapper(r).value
      case _ => Future.successful(None)
    }
    FutureOption(mappedResult)
  }

  def map[T1](mapper: T => T1)(implicit executor: ExecutionContext): FutureOption[T1] =
    FutureOption(value.map(_.map(mapper)))

  def foreach[T1](mapper: T => T1)(implicit executor: ExecutionContext): Unit = {
    value.onComplete(_.foreach(_.foreach(mapper)))
  }
}


trait FutureOptionExtensions {
  implicit class RichFutureOption[T](extendee: Future[Option[T]]) {
    def dumap[T1](mapper: T => Future[Option[T1]])(implicit executor: ExecutionContext): Future[Option[T1]] =
      toFutureOption.rawFlatMap(mapper)

    def toFutureOption: FutureOption[T] = FutureOption(extendee)

  }

  implicit class RichOptionFuture[T](extendee: Option[Future[T]]) {
    def dummed(implicit executor: ExecutionContext): Future[Option[T]] =
      extendee match {
        case Some(r) => r.map(Some(_))
        case _ => Future.successful(None)
      }
  }
}
