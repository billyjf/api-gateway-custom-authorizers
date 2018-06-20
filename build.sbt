name := "api-gateway-custom-authorizers"

version := "0.1"

scalaVersion := "2.12.6"

val circeVersion = "0.9.2"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-core" % "1.11.273",
  "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.273",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.amazonaws" % "aws-lambda-java-events" % "2.0.2",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-optics" % circeVersion,
  "io.circe" %% "circe-jawn" % circeVersion,

  "org.scalatest" %% "scalatest" % "3.0.5",
  "org.scalamock" %% "scalamock" % "4.0.0"
)