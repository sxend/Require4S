import sbt._
import Keys._

object Require4SBuild extends Build {

  lazy val root = Project(id = "Require4S", base = file("."), settings = Project.defaultSettings ++ Seq(
    organization := "arimitsu.sf",
    name := "Require4S",
    version := "0.0.5-SNAPSHOT",
    crossScalaVersions := Seq("2.10.4", "2.11.0"),
    publishTo := Some(Resolver.file("require4s", file("./maven-repo"))(Patterns(true, Resolver.mavenStyleBasePattern))),
    libraryDependencies ++= Seq(
      "com.google.inject" % "guice" % "3.0",
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "org.reflections" % "reflections" % "0.9.7",
      "javax.servlet" % "servlet-api" % "2.5" % "provided",
      "org.scalatest" %% "scalatest" % "2.1.4" % "test",
      "org.scalaz" %% "scalaz-core" % "7.0.6"
    )
  ))

}
