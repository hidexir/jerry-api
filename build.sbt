ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "hidexir.github.io"

lazy val hello = (project in file("."))
  .settings(
    name := "Hello"
  )

