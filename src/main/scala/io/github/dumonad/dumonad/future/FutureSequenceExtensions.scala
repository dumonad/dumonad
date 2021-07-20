package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}

trait FutureSequenceExtensions {
  implicit class RichFutureSequence[T1](extendee: Future[Iterable[T1]]) {
    def dumap[T2](mapper: T1 => Future[Iterable[T2]])(implicit executor: ExecutionContext): Future[Iterable[T2]] =
      extendee.flatMap(seq => Future.sequence(seq.map(mapper)).map(_.flatten))
  }

  implicit class RichSequenceFuture[T](extendee: Iterable[Future[T]]) {
    def dummed(implicit executor: ExecutionContext): Future[Iterable[T]] =
      Future.sequence(extendee)
  }
}
