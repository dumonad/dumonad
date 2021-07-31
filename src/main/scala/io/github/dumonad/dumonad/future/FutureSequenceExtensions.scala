package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait FutureSequenceExtensions {
  implicit class RichFutureSequence[T](extendee: Future[Iterable[T]]) {
    def dumap[T2](mapper: T => Future[Iterable[T2]])(implicit
        executor: ExecutionContext
    ): Future[Iterable[T2]] =
      toFutureSequence.flatMap(mapper(_).toFutureSequence).value

    def toFutureSequence: FutureSequence[T] = FutureSequence(extendee)

  }

  implicit class RichSequenceFuture[T](extendee: Iterable[Future[T]]) {
    def extractFuture(implicit
        executor: ExecutionContext
    ): Future[Iterable[T]] =
      Future.sequence(extendee)
  }

  implicit class RichSequence[T: ClassTag](extendee: Iterable[T]) {
    def toFutureSequence: FutureSequence[T] = FutureSequence(extendee)
  }
}
