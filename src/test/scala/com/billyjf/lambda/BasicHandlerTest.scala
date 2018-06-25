package com.billyjf.lambda

import java.io._
import java.net.URL
import java.util.stream.Collectors

import com.amazonaws.services.lambda.runtime.Context
import io.circe.{Decoder, Json}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.Logger

import scala.collection.JavaConverters._
import scala.io.Source
import io.circe.parser._
import io.circe.generic.auto._
import io.circe.syntax._

/** To pass into any handleRequest invocation for a given jsonResource. */
case class JSONIn(jsonResource: String) {
  val resource: URL = getClass.getResource(jsonResource)
  val body: String = new BufferedReader(Source.fromURL(resource).reader()).lines().collect(Collectors.joining("\n"))
  val inputStream: FileInputStream = new FileInputStream(new File(resource.toURI))
}

class BasicHandlerTest extends FlatSpec with Matchers with MockFactory {
  val log: Logger = mock[Logger]
  (log.info(_: String)).expects(*).atLeastOnce()

  val underTest = new BasicHandler(log)

  val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream()

  "handleRequest" must "return and log a valid response to API Gateway when provided a valid hard coded user and password" in {
    val jsonIn: JSONIn = JSONIn("/apiGatewayIn.json")
    println(s"Given\n${jsonIn.body}\n")

    underTest.handleRequest(jsonIn.inputStream, outputStream, null)

    val returnedPolicy: Policy = parse(outputStream.toString).right.get.as[Policy].right.get

    println(s"Allow\n${returnedPolicy.asJson}")
  }

  "handleRequest" must "throw an exception when provided an invalid password" in {  // TODO

  }
}
