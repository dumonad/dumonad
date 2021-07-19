lazy val scala212 = "2.12.14"
lazy val scala213 = "2.13.6"
lazy val scala3 = "3.0.0"
lazy val supportedScalaVersions = List(scala212, scala213, scala3)

crossScalaVersions := supportedScalaVersions

name := "dummonad"
version := "0.1"
organization := "io.github.dumonad"

homepage := Some(url("https://dumonad.github.io/"))
scmInfo := Some(ScmInfo(url("https://github.com/dumonad/dumonad"), "git@github.com:dataoperandz/cassper.git"))
developers := List(Developer("mohsenkashi", "Mohsen", "mhmhkashi@gmail.com", url("https://github.com/mohsenkashi")))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else {
//    Opts.resolver.sonatypeStaging
    MavenRepository(
      "sonatype-staging",
      "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
    )
  }
)

sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.9" % "test",
  "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % "test"
)