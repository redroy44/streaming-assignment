import Dependencies._

val scala3Version = "3.1.2"

lazy val root = project
  .in(file("."))
  .aggregate(streamingApp)

lazy val streamingApp = project
  .in(file("streaming"))
  .settings(
    name := "streaming-app",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= fs2 ++ cats ++ logging
  )
