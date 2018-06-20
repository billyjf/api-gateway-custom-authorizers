package com.billyjf.lambda

import com.amazonaws.services.lambda.runtime.Context
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.Logger

import scala.collection.JavaConverters._

class JWTHandlerTest extends FlatSpec with Matchers with MockFactory {
  "handleRequest" must "return a valid response to API Gateway" in {
    val log = mock[Logger]
    //(log.info(_: String)).expects(*).atLeastOnce()  // TODO Mock

    val underTest = new JWTHandler(log)

    println(underTest.response)
  }
}
