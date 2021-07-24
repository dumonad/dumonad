package io.github.dumonad.dumonad.future

import io.github.dumonad.dumonad.Implicits.{RichOptionFuture, RichFutureOption}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureOptionSpecs extends AsyncFlatSpec with Matchers {

  def futureOfSome: Future[Option[String]] = Future.successful(Some("Happy"))

  def futureOfNone: Future[Option[String]] = Future.successful(None)

  class MockedScope {
    def mapper(param: String): Future[Option[String]] = Future.successful(Some(s"${param}2"))
  }

  "FutureOption" should "act well in a for-comprehension" in {
    val spy = Mockito.spy(new MockedScope)

    val comprehensionResult = for {
      some <- futureOfSome.toFutureOption
      callResult <- spy.mapper(some).toFutureOption
    } yield callResult

    comprehensionResult.value.map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Some("Happy2")
    }
  }
  it should "not cal call mapper for none for-comprehension" in {
    val spy = Mockito.spy(new MockedScope)

    val comprehensionResult = for {
      some <- futureOfNone.toFutureOption
      callResult <- spy.mapper(some).toFutureOption
    } yield callResult

    comprehensionResult.value.map { r =>
      verify(spy,times(0)).mapper(any[String])
      r shouldBe None
    }
  }

  "RichFutureOption" should "map Some" in {

    val spy = Mockito.spy(new MockedScope)
    futureOfSome.dumap(spy.mapper) map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Some("Happy2")
    }
  }

  it should "not call the mapper for a None" in {
    val spy = Mockito.spy(new MockedScope)
    futureOfNone.dumap(spy.mapper) map { l =>
      verify(spy, times(0)).mapper(any[String])
      l shouldBe None
    }
  }

  "RichOptionFuture" should "convert a Some[Future] to Future[Some]" in {
    Some(Future.successful("Happy")).extractFuture.map(_ shouldBe Some("Happy"))
  }

  it should "convert a None[Future] to Future[None]" in {
    None.extractFuture.map(_ shouldBe None)
  }

}
