package io.github.dumonad.dumonad.future

import io.github.dumonad.dumonad.Implicits._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureSequenceExtensionsSpecs extends AsyncFlatSpec with Matchers {
  class MockedScope {
    def mapper(param: String): Future[Seq[String]] = Future.successful(Seq(s"${param}2"))
  }

  "RichFutureSeq" should "map Seq" in {
    def futureOfSeq: Future[Seq[String]] = Future.successful(Seq("Happy"))

    val spy = Mockito.spy(new MockedScope)
    futureOfSeq.dumap(spy.mapper) map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Seq("Happy2")
    }
  }

  it should "not call the mapper for an empty Seq" in {
    def futureOfSeq: Future[Seq[String]] = Future.successful(Seq())

    val spy = Mockito.spy(new MockedScope)
    futureOfSeq.dumap(spy.mapper) map { l =>
      verify(spy, times(0)).mapper(any[String])
      l shouldBe Seq()
    }
  }

  "RichSeqFuture" should "convert a Seq[Future] to Future[Seq]" in {
    Seq(Future.successful("Happy")).dummed.map(_ shouldBe Seq("Happy"))
  }


}
