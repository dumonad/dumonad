package com.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}

trait FutureEitherExtensions {
  implicit class RichFutureEither[L, R1](extendee: Future[Either[L, R1]]) {
    def dumap[R2](mapper: R1 => Future[Either[L, R2]])(implicit executor: ExecutionContext): Future[Either[L, R2]] =
      extendee.flatMap {
        case Right(value) => mapper(value)
        case Left(l) => Future.successful(Left(l))
      }
  }

  implicit class RichEitherFuture[L, R](extendee: Either[L, Future[R]]) {
    def dummed(implicit executor: ExecutionContext): Future[Either[L, R]] =
      extendee match {
        case Right(r) => r.map(Right(_))
        case Left(l) => Future.successful(Left(l))
      }

  }
}
