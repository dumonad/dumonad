package com.github.dumonad.dumonad.future

import com.github.dumonad.dumonad.Implicits.{RichEitherFuture, RichFutureEither}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureEitherExtensionsSpecs extends AsyncFlatSpec with Matchers {
  object MockedScope {
    def mapper(param: String): Future[Either[String, String]] = Future.successful(Right(s"${param}2"))
  }

  "RichFutureEither" should "map Right" in {
    def futureOfRight: Future[Either[String, String]] = Future.successful(Right("Happy"))

    val spy = Mockito.spy(MockedScope)
    futureOfRight.dumap(spy.mapper) map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Right("Happy2")
    }
  }

  it should "not call the mapper for a Left" in {
    def futureOfLeft: Future[Either[String, String]] = Future.successful(Left("Sad"))

    val spy = Mockito.spy(MockedScope)
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
