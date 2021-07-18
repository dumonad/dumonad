package com.github.dumonad.dumonad.future

import com.github.dumonad.dumonad.Implicits.{RichOptionFuture, RichFutureOption}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureOptionExtensionsSpecs extends AsyncFlatSpec with Matchers {
  object MockedScope {
    def mapper(param: String): Future[Option[String]] = Future.successful(Some(s"${param}2"))
  }

  "RichFutureOption" should "map Some" in {
    def futureOfSome: Future[Option[String]] = Future.successful(Some("Happy"))

    val spy = Mockito.spy(MockedScope)
    futureOfSome.dumap(spy.mapper) map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Some("Happy2")
    }
  }

  it should "not call the mapper for a None" in {
    def futureOfNone: Future[Option[String]] = Future.successful(None)

    val spy = Mockito.spy(MockedScope)
    futureOfNone.dumap(spy.mapper) map { l =>
      verify(spy, times(0)).mapper(any[String])
      l shouldBe None
    }
  }

  "RichOptionFuture" should "convert a Some[Future] to Future[Some]" in {
    Some(Future.successful("Happy")).dummed.map(_ shouldBe Some("Happy"))
  }

  it should "convert a None[Future] to Future[None]" in {
    None.dummed.map(_ shouldBe None)
  }

}
