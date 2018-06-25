package com.billyjf.lambda

import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
import java.util.Base64
import java.util.stream.Collectors

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import org.slf4j.{Logger, LoggerFactory}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._

case class Policy(principalId: String, policyDocument: PolicyDocument)

case class PolicyDocument(Statement: List[Statement]) {
  val Version: String = "2012-10-17"
}

case class Statement(Action: String, Effect: String, Resource: String)

case class AuthorizerIn(`type`: String, methodArn: String, authorizationToken: String)

case class BasicAuthorizationToken(BasicAuthorizationToken: String) {
  val decoded: String = new String(Base64.getDecoder.decode(BasicAuthorizationToken.split("Basic ")(1)))
  val parts: Array[String] = decoded.split(':')
  val user: String = parts(0)
  val password: String = parts(1)
}

class BasicHandler(log: Logger) extends RequestStreamHandler {
  def this() = this(log = LoggerFactory.getLogger(getClass.getSimpleName))

  val EXECUTE_API_INVOKE: String = "execute-api:Invoke"
  val ALLOW: String = "Allow"
  val DENY: String = "Deny"
  val ANY_API: String = "arn:aws:execute-api:*:*:*"

  val HARD_CODED_VALID_USER: String = "apiGateway"
  val HARD_CODED_VALID_PASSWORD: String = "isAwesome"

  def handleRequest(is: InputStream, os: OutputStream, context: Context): Unit = {
    val isJsonBody: String = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"))
    val policyIn: AuthorizerIn = parse(isJsonBody).right.get.as[AuthorizerIn].right.get
    val authorization: BasicAuthorizationToken = BasicAuthorizationToken(policyIn.authorizationToken)

    def isAuthorized: Boolean = {
      authorization.user.equals(HARD_CODED_VALID_USER) && authorization.password.equals(HARD_CODED_VALID_PASSWORD)
    }

    val statement: Statement = Statement(
      EXECUTE_API_INVOKE,
      if (isAuthorized) ALLOW else DENY,
      s"$ANY_API/prod/GET/users"
    )

    log.info(s"${authorization.user} is ${if (!isAuthorized) "not"} authorized.")

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

    os.write(
      Policy("xyz",
        PolicyDocument(
          List(
            statement
          )
        )
      ).asJson.toString.getBytes
    )
  }
}
