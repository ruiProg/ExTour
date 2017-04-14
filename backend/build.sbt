name := """ExTour"""

version := "1.0-SNAPSHOT"

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  "mysql" % "mysql-connector-java" % "6.0.6",
  cache,
  javaWs
)

fork in run := true