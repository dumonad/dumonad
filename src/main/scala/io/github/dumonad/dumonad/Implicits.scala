package io.github.dumonad.dumonad

import io.github.dumonad.dumonad.future.{
  FutureEitherExtensions,
  FutureOptionExtensions,
  FutureSequenceExtensions
}
import io.github.dumonad.dumonad.option.OptionExtensions
import io.github.dumonad.dumonad.seq.SequenceEitherExtensions

object Implicits
    extends FutureOptionExtensions
    with FutureEitherExtensions
    with FutureSequenceExtensions
    with SequenceEitherExtensions
    with OptionExtensions
