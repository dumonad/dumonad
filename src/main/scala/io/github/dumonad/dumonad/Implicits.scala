package io.github.dumonad.dumonad

import io.github.dumonad.dumonad.future.{FutureEitherExtensions, FutureOptionExtensions}

object Implicits
  extends FutureOptionExtensions
    with FutureEitherExtensions
