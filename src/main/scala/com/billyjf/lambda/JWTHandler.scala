package com.billyjf.lambda

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import org.slf4j.{Logger, LoggerFactory}
import io.circe.generic.auto._
import io.circe.syntax._

case class Policy(principalId: String, policyDocument: PolicyDocument)

case class PolicyDocument(Statement: List[Statement]) {
  val Version: String = "2012-10-17"
}

case class Statement(Action: String, Effect: String, Resource: String)

class JWTHandler(log: Logger) extends RequestStreamHandler {
  def this() = this(log = LoggerFactory.getLogger(getClass.getSimpleName))

  val response: String = Policy("xyz",
    PolicyDocument(
      List(
        Statement("execute-api:Invoke", "Allow", "arn:aws:execute-api:*:*:*/prod/GET/users")
      )
    )
  ).asJson.toString

  def handleRequest(is: InputStream, os: OutputStream, context: Context): Unit = {

    // TODO de-serialize is
    // validate the incoming token// validate the incoming token

    // and produce the principal user identifier associated with the token

    // this could be accomplished in a number of ways:
    // 1. Call out to OAuth provider
    // 2. Decode a JWT token in-line
    // 3. Lookup in a self-managed DB
    val principalId = "xxxx"

    // if the client token is not recognized or invalid
    // you can send a 401 Unauthorized response to the client by failing like so:
    // throw new RuntimeException("Unauthorized");

    // if the token is valid, a policy should be generated which will allow or deny access to the client

    // if access is denied, the client will receive a 403 Access Denied response
    // if access is allowed, API Gateway will proceed with the back-end integration configured on the method that was called

    // TODO
    /*val methodArn = input.getMethodArn
    val arnPartials = methodArn.split(":")
    val region = arnPartials(3)
    val awsAccountId = arnPartials(4)
    val apiGatewayArnPartials = arnPartials(5).split("/")
    val restApiId = apiGatewayArnPartials(0)
    val stage = apiGatewayArnPartials(1)
    val httpMethod = apiGatewayArnPartials(2)
    var resource = "" // root resource
    if (apiGatewayArnPartials.length == 4) resource = apiGatewayArnPartials(3)*/

    // this function must generate a policy that is associated with the recognized principal user identifier.
    // depending on your use case, you might store policies in a DB, or generate them on the fly

    // keep in mind, the policy is cached for 5 minutes by default (TTL is configurable in the authorizer)
    // and will apply to subsequent calls to any method/resource in the RestApi
    // made with the same token

    log.info("Invoked.")

    os.write(response.getBytes)
  }
}
