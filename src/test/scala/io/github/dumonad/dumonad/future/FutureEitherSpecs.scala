package io.github.dumonad.dumonad.future

import io.github.dumonad.dumonad.Implicits._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureEitherSpecs extends AsyncFlatSpec with Matchers {
  def futureOfRight: Future[Either[String, String]] = Future.successful(Right("Happy"))
  def futureOfLeft: Future[Either[String, String]] = Future.successful(Left("Sad"))

  class MockedScope {
    def mapper(param: String): Future[Either[String, String]] = Future.successful(Right(s"${param}2"))
  }


  "FutureEither" should "act well in a for-comprehension" in {
    val spy = Mockito.spy(new MockedScope)

    val comprehensionResult = for {
      right <- futureOfRight.toFutureEither
      callResult <- spy.mapper(right).toFutureEither
    } yield callResult

    comprehensionResult.value.map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Right("Happy2")
    }
  }
  it should "not cal call mapper for Left for-comprehension" in {
    val spy = Mockito.spy(new MockedScope)

    val comprehensionResult = for {
      right <- futureOfLeft.toFutureEither
      callResult <- spy.mapper(right).toFutureEither
    } yield callResult

    comprehensionResult.value.map { r =>
      verify(spy,times(0)).mapper(any[String])
      r shouldBe Left("Sad")
    }
  }

  "RichFutureEither" should "map Right" in {
    val spy = Mockito.spy(new MockedScope)
    futureOfRight.dumap(spy.mapper) map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Right("Happy2")
    }
  }

  it should "not call the mapper for a Left" in {
    val spy = Mockito.spy(new MockedScope)
    futureOfLeft.dumap(spy.mapper) map { l =>
      verify(spy, times(0)).mapper(any[String])
      l shouldBe Left("Sad")
    }
  }

  "RichEitherFuture" should "convert a Right[Future] to Future[Right]" in {
    Right(Future.successful("Happy")).dummed.map(_ shouldBe Right("Happy"))
  }

  it should "convert a Left[Future] to Future[Left]" in {
    Left("Sad").dummed.map(_ shouldBe Left("Sad"))
  }

}
