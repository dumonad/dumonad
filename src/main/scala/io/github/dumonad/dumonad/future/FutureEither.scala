package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}
import scala.util.Either

case class FutureEither[L, R](value: Future[Either[L, R]]) {

  def flatMap[R1](
      mapper: R => FutureEither[L, R1]
  )(implicit executor: ExecutionContext): FutureEither[L, R1] = {
    val mappedResult = value.flatMap {
      case Right(r) => mapper(r).value
      case Left(l)  => Future.successful(Left[L, R1](l))
    }
    FutureEither(mappedResult)
  }

  def flatMapLeft[L1](
      mapper: L => FutureEither[L1, R]
  )(implicit executor: ExecutionContext): FutureEither[L1, R] = {
    val mappedResult = value.flatMap {
      case Right(r) => Future.successful(Right[L1, R](r))
      case Left(l)  => mapper(l).value
    }
    FutureEither(mappedResult)
  }

  def map[R1](mapper: R => R1)(implicit
      executor: ExecutionContext
  ): FutureEither[L, R1] =
    FutureEither(value.map(_.map(mapper)))

  def mapLeft[L1](mapper: L => L1)(implicit
      executor: ExecutionContext
  ): FutureEither[L1, R] =
    FutureEither(value.map(_.left.map(mapper)))

  def foreach[R1](
      mapper: R => R1
  )(implicit executor: ExecutionContext): Unit = {
    value.onComplete(_.foreach(_.foreach(mapper)))
  }
}

object FutureEither {
  def apply[L, R: ClassTag](e: Either[L, R]): FutureEither[L, R] = {
    require(
      !classTag[R].equals(classTag[Future[_]]),
      "You are trying to generate Future[Either[L,Future[R]] which increases the complexity. Use `extractFuture` method instead"
    )
    this(Future.successful(e))
  }
}
