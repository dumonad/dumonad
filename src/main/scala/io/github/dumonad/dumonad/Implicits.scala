package io.github.dumonad.dumonad

import io.github.dumonad.dumonad.future.{FutureEitherExtensions, FutureOptionExtensions, FutureSequenceExtensions}

object Implicits
  extends FutureOptionExtensions
    with FutureEitherExtensions
    with FutureSequenceExtensions
