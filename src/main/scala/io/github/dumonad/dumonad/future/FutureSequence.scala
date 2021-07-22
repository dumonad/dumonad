package io.github.dumonad.dumonad.future

import scala.concurrent.{ExecutionContext, Future}

case class FutureSequence[T](value: Future[Iterable[T]]) {

  def rawFlatMap[T1](mapper: T => Future[Iterable[T1]])(implicit executor: ExecutionContext): Future[Iterable[T1]] =
    value.flatMap(seq => Future.sequence(seq.map(mapper)).map(_.flatten))


  def flatMap[T1](mapper: T => FutureSequence[T1])(implicit executor: ExecutionContext): FutureSequence[T1] = {
    val mappedResult = value.flatMap(seq => Future.sequence(seq.map(mapper).map(_.value)).map(_.flatten))
    FutureSequence(mappedResult)
  }

  def map[T1](mapper: T => T1)(implicit executor: ExecutionContext): FutureSequence[T1] =
    FutureSequence(value.map(_.map(mapper)))


  def foreach[T1](mapper: T => T1)(implicit executor: ExecutionContext) {
    value.onComplete(_.foreach(_.foreach(mapper)))
  }
}


trait FutureSequenceExtensions {
  implicit class RichFutureSequence[T](extendee: Future[Iterable[T]]) {
    def dumap[T2](mapper: T => Future[Iterable[T2]])(implicit executor: ExecutionContext): Future[Iterable[T2]] =
      toFutureSequence.rawFlatMap(mapper)

    def toFutureSequence: FutureSequence[T] = FutureSequence(extendee)

  }

  implicit class RichSequenceFuture[T](extendee: Iterable[Future[T]]) {
    def dummed(implicit executor: ExecutionContext): Future[Iterable[T]] =
      Future.sequence(extendee)
  }
}
