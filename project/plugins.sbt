// sbt-sonatype plugin used to publish artifact to maven central via sonatype nexus
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.7")
// sbt-pgp plugin used to sign the artifcat with pgp keys
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.3")