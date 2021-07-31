package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.Either

trait FutureEitherExtensions {
  implicit class RichFutureEither[L, R](extendee: Future[Either[L, R]]) {
    def dumap[R1](mapper: R => Future[Either[L, R1]])(implicit
        executor: ExecutionContext
    ): Future[Either[L, R1]] =
      toFutureEither.flatMap(mapper)

    def toFutureEither: FutureEither[L, R] = FutureEither(extendee)
  }

  implicit class RichEitherFuture[L, R](extendee: Either[L, Future[R]]) {
    def extractFuture(implicit
        executor: ExecutionContext
    ): Future[Either[L, R]] =
      extendee match {
        case Right(r) => r.map(Right(_))
        case Left(l)  => Future.successful(Left(l))
      }

  }

  implicit class RichEither[L, R: ClassTag](extendee: Either[L, R]) {
    def toFutureEither: FutureEither[L, R] = FutureEither(extendee)
  }
}
