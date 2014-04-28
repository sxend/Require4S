import sbt._
import Keys._

object Require4SBuild extends Build {

  lazy val root = Project(id = "Require4S", base = file("."), settings = Project.defaultSettings ++ Seq(
    organization := "arimitsu.sf",
    name := "Require4S",
    version := "0.0.1",
    scalaVersion := "2.10.4",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.1.4" % "test",
      "org.scalaz" %% "scalaz-core" % "7.0.6"
    )
  ))

}