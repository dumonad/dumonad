# Dumonad

Dumonad is a solution to ease of using monad in Scala projects with introducing a set of extension methods and wrapper types.

[KISS](https://en.wikipedia.org/wiki/KISS_principle) your code with Dumonad!

Reactive programming become more and more popular nowadays. A common response of a reactive call is a nested monad
(i.e. `Future[Either[L,R]]` or `Future[Option[T]]`)

All scala developers know what are `map`, `flatmap`, and `flatten`

The shortest way of chaining two `Future[Either[L,R]]` using standard Scala API will be something like this:

```scala

def firstCall: Future[Either[String, String]] = ???
def secondCall(param: String): Future[Either[String, String]] = ???

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

### for-comprehension

Beside extension methods, Dumonad also represents a set of wrapper classes for nested types(`FutureOption`
,`FutureEither`,...)

To wrap the base model you can use `apply` method or related extension method:

```scala
import io.github.dumonad.dumonad.future.FutureEither

val maybeCustomer: Future[Option[Customer]] 
def persistCustomer(customer: Customer): Future[Either[String, PersistResult]]
for {
  customer <- maybeCustomer.toFutureRight("customer not found")
  persistResult <- persistCustomer(customer).toFutureeither
} yield persistResult
```
### Helper methods

Another thing is changing the order of monad wrappers.  
Developers usually use boilerplate codes to do this. For example:

```scala

val optionOfFuture: Option[Future[String]]

val futureOfOption: Future[Option[String]] = optionOfFuture match {
  case Some(future) => future.map(Some(_))
  case _ => Future.successful(None)
}
```

With `extractFuture` method it will be a piece of cake, because **in Dumonad `Future` is always the outer wrapper.**

```scala

val optionOfFuture: Option[Future[String]]

val futureOfOption: Future[Option[String]] = optionOfFuture.extractFuture
```

Another helper method is `extractEither` that converts `Seq[Either[L,R]]` to `Either[Seq[L],Seq[R]]`

## Getting started

_supported scala versions : 2.12, 2.13, and 3.0_

Add the dependency in build.sbt

```scala
libraryDependencies += "io.github.dumonad" %% "dummonad" % "0.5"
```

It's all set!

```scala
import io.github.dumonad.dumonad.Implicits._

validate(customer).dumap(persist).dumap(notifyAdmin)
```
