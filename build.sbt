//import sbt.Keys._
import sbt._


name := "akka-http-helloworld"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
   "com.typesafe.akka" %% "akka-http"   % "10.1.9",
   "com.typesafe.akka" %% "akka-stream" % "2.5.23",
   "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9",
   "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"
)

