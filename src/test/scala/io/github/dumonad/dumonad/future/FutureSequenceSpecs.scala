package io.github.dumonad.dumonad.future

import io.github.dumonad.dumonad.Implicits._
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class FutureSequenceSpecs extends AsyncFlatSpec with Matchers {
  class MockedScope {
    def mapper(param: String): Future[Seq[String]] = Future.successful(Seq(s"${param}1", s"${param}2"))
  }

  def futureOfSeq: Future[Seq[String]] = Future.successful(Seq("Happy", "Thrilled"))


  "FutureSequence" should "act well in a for-comprehension" in {
    val spy = Mockito.spy(new MockedScope)

    val comprehensionResult = for {
      mood <- futureOfSeq.toFutureSequence
      callResult <- spy.mapper(mood).toFutureSequence
    } yield callResult

    comprehensionResult.value.map { r =>
      verify(spy).mapper("Happy")
      verify(spy).mapper("Thrilled")
      r shouldBe Seq("Happy1", "Happy2", "Thrilled1", "Thrilled2")
    }
  }

  "FutureSequence" should "support if in a for-comprehension" in {
    val spy = Mockito.spy(new MockedScope)

    val comprehensionResult = for {
      mood <- futureOfSeq.toFutureSequence
      if mood.equals("Happy")
      callResult <- spy.mapper(mood).toFutureSequence
    } yield callResult

    comprehensionResult.value.map { r =>
      verify(spy).mapper("Happy")
      verify(spy,times(0)).mapper("Thrilled")
      r shouldBe Seq("Happy1", "Happy2")
    }
  }


  "RichFutureSeq" should "map Seq" in {

    val spy = Mockito.spy(new MockedScope)
    futureOfSeq.dumap(spy.mapper) map { r =>
      verify(spy).mapper("Happy")
      r shouldBe Seq("Happy1", "Happy2", "Thrilled1", "Thrilled2")
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
    Seq(Future.successful("Happy")).extractFuture.map(_ shouldBe Seq("Happy"))
  }


}
