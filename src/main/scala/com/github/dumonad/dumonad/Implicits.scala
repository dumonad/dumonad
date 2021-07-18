package com.github.dumonad.dumonad

import com.github.dumonad.dumonad.future.{FutureEitherExtensions, FutureOptionExtensions}

object Implicits
  extends FutureOptionExtensions
    with FutureEitherExtensions
