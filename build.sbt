lazy val scala212 = "2.12.14"
lazy val scala213 = "2.13.6"
lazy val scala3 = "3.0.0"
lazy val supportedScalaVersions = List(scala212, scala213, scala3)

name := "dummonad"

version := "0.1"

ThisBuild / scalaVersion := scala3

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % "test"