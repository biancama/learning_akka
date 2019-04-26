name := """akkademy-db"""

organization := "com.akkademy.db"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.8"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.7" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.22"
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.5.22"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.22" % Test


mappings in (Compile, packageBin) ~= { _.filterNot { case (_, name) =>
  Seq("application.conf").contains(name)
}}