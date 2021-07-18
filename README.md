# Dumonad

Dumonad is a solution to ease of using monad in Scala projects with introducing a set of extension methods.

[KISS](https://en.wikipedia.org/wiki/KISS_principle) your code with Dumonad!

Reactive programming become more and more popular nowadays. A common response of a reactive call is a nested monad(
i.e `Future[Either[L,R]]` or `Future[Option[T]]`)

All scala developers know what are `map`, `flatmap`, and `flatten`

The shortest way of chaining two `FutureEither` using standard Scala API will be something like this:

```scala

def firstCall: Future[Either[String, String]]
def secondCall(param: String): Future[Either[String, String]]

val result: Future[Either[String, String]] = firstCall.flatMap {
  case Right(value) => secondCall(value)
  case left => Future.successful(left)
}
```

While using Dumonad it will be easier empowered with `dumap`:

```scala

def firstCall: Future[Either[String, String]]
def secondCall(param: String): Future[Either[String, String]]

val result: Future[Either[String, String]] = firstCall.dumap(secondCall)
```

Another thing is changing the order of monad wrappers.  
Developers usually use boilerplate codes to do this. For example:

```scala

val optionOfFuture: Option[Future[String]]

val futureOfOption: Future[Option[String]] = optionOfFuture match {
  case Some(future) => future.map(Some(_))
  case _ => Future.successful(None)
}
```

With `dummed` method it will be a piece of cake, because **in Dumonad `Future` is always the outer wrapper.**

```scala

val optionOfFuture: Option[Future[String]]

val futureOfOption: Future[Option[String]] = optionOfFuture.dummed
```
