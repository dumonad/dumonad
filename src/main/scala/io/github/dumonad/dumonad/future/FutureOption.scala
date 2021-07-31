package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}

case class FutureOption[T](value: Future[Option[T]]) {

  def flatMap[T1](
      mapper: T => Future[Option[T1]]
  )(implicit executor: ExecutionContext): Future[Option[T1]] = {
    value.flatMap {
      case Some(r) => mapper(r)
      case _       => Future.successful(None)
    }
  }

  def flatMap[T1](
      mapper: T => FutureOption[T1]
  )(implicit executor: ExecutionContext): FutureOption[T1] = {
    val mappedResult = value.flatMap {
      case Some(r) => mapper(r).value
      case _       => Future.successful(None)
    }
    FutureOption(mappedResult)
  }

  def map[T1](mapper: T => T1)(implicit
      executor: ExecutionContext
  ): FutureOption[T1] =
    FutureOption(value.map(_.map(mapper)))

  def foreach[T1](
      mapper: T => T1
  )(implicit executor: ExecutionContext): Unit = {
    value.onComplete(_.foreach(_.foreach(mapper)))
  }

  def filter(
      p: T => Boolean
  )(implicit executor: ExecutionContext): FutureOption[T] = {
    FutureOption(value.map(_.filter(p)))
  }

  def withFilter(p: T => Boolean)(implicit
      executor: ExecutionContext
  ): FutureOption[T] = filter(p)

  def toFutureRight[L](left: L)(implicit executor: ExecutionContext): FutureEither[L, T] =
    FutureEither(value.map(_.toRight(left)))

  def toFutureLeft[R](right: R)(implicit executor: ExecutionContext): FutureEither[T, R] =
    FutureEither(value.map(_.toLeft(right)))

  def toFutureSequence(implicit executor: ExecutionContext): FutureSequence[T] =
    FutureSequence(value.map(_.toSeq))

}

object FutureOption {
  def apply[T: ClassTag](e: Option[T]): FutureOption[T] = {
    require(
      !classTag[T].equals(classTag[Future[_]]),
      "You are trying to generate Future[Option[Future[T]] which increases the complexity. Use `extractFuture` method instead"
    )
    this(Future.successful(e))
  }
}
