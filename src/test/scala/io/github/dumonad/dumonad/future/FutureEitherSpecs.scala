package io.github.dumonad.dumonad.future

import io.github.dumonad.dumonad.Implicits._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureEitherSpecs extends AsyncFlatSpec with Matchers {

  private val right: Either[String, String] = Right("Happy")
  private val left: Either[String, String] = Left("Sad")

  def futureOfRight: Future[Either[String, String]] = Future.successful(right)
  def futureOfLeft: Future[Either[String, String]] = Future.successful(left)

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

    comprehensionResult.value.map { e =>
      verify(spy, times(0)).mapper(any[String])
      e shouldBe left
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
      l shouldBe left
    }
  }

  "RichEitherFuture" should "convert a Right[Future] to Future[Right]" in {
    Right(Future.successful("Happy")).extractFuture.map(_ shouldBe right)
  }

  it should "convert a Left[Future] to Future[Left]" in {
    Left[String, Future[String]]("Sad").extractFuture.map(_ shouldBe left)
  }

  "RichEither" should "convert an either to FutureEither" in {
    right.toFutureEither.value.map(_ shouldBe right)
  }
  it should "generate error to wrap a Either[Future] with Future" in {
    intercept[Exception] {
      Right[String, Future[String]](Future.successful("Sad")).toFutureEither.value.map(_ shouldBe right)
    }.getMessage shouldBe "requirement failed: You are trying to generate Future[Either[L,Future[R]] which increases the complexity. Use `extractFuture` method instead"
  }

}
