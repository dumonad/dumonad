package io.github.dumonad.dumonad.seq

trait SequenceEitherExtensions {
  implicit class RichSequenceEither[L, R](extendee: Iterable[Either[L, R]]) {
    def extractEither: Either[Iterable[L], Iterable[R]] = {
      extendee.foldLeft(
        Right(Iterable.empty): Either[Iterable[L], Iterable[R]]
      ) {
        case (accumulator, Right(r)) => accumulator.map(_ ++ Some(r))
        case (Right(_), Left(l))     => Left(Iterable(l))
        case (accumulator, Left(l))  => accumulator.left.map(_ ++ Some(l))
      }
    }
  }
}
