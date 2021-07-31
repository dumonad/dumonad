package io.github.dumonad.dumonad.option

import io.github.dumonad.dumonad.future.{FutureEither, FutureOption, FutureSequence}

import scala.reflect.ClassTag

trait OptionExtensions {
  implicit class RichOption[T: ClassTag](extendee: Option[T]) {
    def toFutureOption: FutureOption[T] =
      FutureOption(extendee)

    def toFutureSequence: FutureSequence[T] =
      FutureSequence(extendee)

    def toFutureRight[L](left: L): FutureEither[L, T] =
      FutureEither(extendee.toRight(left))

    def toFutureLeft[R: ClassTag](right: R): FutureEither[T, R] =
      FutureEither(extendee.toLeft(right))
  }

}
