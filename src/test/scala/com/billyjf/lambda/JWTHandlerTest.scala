package com.billyjf.lambda

import java.io._
import java.net.URL
import java.util.stream.Collectors

import com.amazonaws.services.lambda.runtime.Context
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.Logger

import scala.collection.JavaConverters._
import scala.io.Source

class JWTHandlerTest extends FlatSpec with Matchers with MockFactory {
  "handleRequest" must "return a valid response to API Gateway" in {
    val log = mock[Logger]
    (log.info(_: String)).expects(*).atLeastOnce()

    val underTest = new JWTHandler(log)

    val resource: URL = getClass.getResource("/apiGatewayIn.json")
    val apiGatewayIn: String = new BufferedReader(Source.fromURL(resource).reader()).lines().collect(Collectors.joining("\n"))
    val inputStream: FileInputStream = new FileInputStream(new File(resource.toURI))
    val outputStream: ByteArrayOutputStream = new ByteArrayOutputStream()

    println(s"Given\n$apiGatewayIn")

    underTest.handleRequest(inputStream, outputStream, null)

    println(s"\nAllow\n${outputStream.toString}")
  }
}
