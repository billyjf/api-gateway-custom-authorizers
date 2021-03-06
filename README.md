[![Build Status](https://travis-ci.org/billyjf/api-gateway-custom-authorizers.svg?branch=master)](https://travis-ci.org/billyjf/api-gateway-custom-authorizers)

# AWS API Gateway Custom Authorizers
This serves as the minimum necessary to implement several different auth methods for API Gateway.

## Basic

## JWT

## SAML SSO

## Deployment
```bash
sbt assembly
aws s3 cp target/scala-2.12/api-gateway-custom-authorizers-assembly-0.1.jar s3://api-gateway-auth-poc/jars/
aws cloudformation deploy --stack-name api-gateway-jwt-authorizer-poc-lambda --template-file cloudformation/lambda.yaml --capabilities CAPABILITY_NAMED_IAM
```

## Testing
`curl -X GET --header "Authorization: apiGatewayRocks" https://so7o3tr66d.execute-api.us-west-2.amazonaws.com/prod/users`