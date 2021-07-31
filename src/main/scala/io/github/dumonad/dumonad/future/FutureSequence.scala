package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}

case class FutureSequence[T](value: Future[Iterable[T]]) {

  def flatMap[T1](mapper: T => Future[Iterable[T1]])(implicit
      executor: ExecutionContext
  ): Future[Iterable[T1]] =
    value.flatMap(seq => Future.sequence(seq.map(mapper)).map(_.flatten))

  def flatMap[T1](
      mapper: T => FutureSequence[T1]
  )(implicit executor: ExecutionContext): FutureSequence[T1] = {
    val mappedResult =
      value.flatMap(seq => Future.sequence(seq.map(mapper).map(_.value)).map(_.flatten))
    FutureSequence(mappedResult)
  }

  def map[T1](mapper: T => T1)(implicit
      executor: ExecutionContext
  ): FutureSequence[T1] =
    FutureSequence(value.map(_.map(mapper)))

  def foreach[T1](
      mapper: T => T1
  )(implicit executor: ExecutionContext): Unit = {
    value.onComplete(_.foreach(_.foreach(mapper)))
  }

  def filter(
      p: T => Boolean
  )(implicit executor: ExecutionContext): FutureSequence[T] = {
    FutureSequence(value.map(_.filter(p)))
  }

  def withFilter(p: T => Boolean)(implicit
      executor: ExecutionContext
  ): FutureSequence[T] = filter(p)

}

object FutureSequence {
  def apply[T: ClassTag](e: Iterable[T]): FutureSequence[T] = {
    require(
      !classTag[T].equals(classTag[Future[_]]),
      "You are trying to generate Future[Iterable[Future[T]] which increases the complexity. Use `extractFuture` method instead"
    )
    this(Future.successful(e))
  }
}
