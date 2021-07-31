package io.github.dumonad.dumonad.seq

import io.github.dumonad.dumonad.Implicits.RichSequenceEither
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SequenceEitherExtensionsSpecs extends AnyFlatSpec with Matchers {
  "extractEither" should "create a Left if there is at least one left in sequence" in {
    Seq(Right("1"), Left(-1), Left(-2), Right("2")).extractEither shouldBe Left(Seq(-1, -2))
  }

  it should "create a Right if there all elements are right" in {
    Seq(Right(1), Right(2), Right(3)).extractEither shouldBe Right(Seq(1, 2, 3))
  }

  it should "create a Right for empty sequence" in {
    Seq().extractEither shouldBe Right(Seq())
  }
}
