# Tinybank

### Assumptions are documented in Assumptions.md

### To start:

#### build:

`./gradlew clean build`

#### start server

`./gradlew bootRun`

#### For example interactions refer to "requests.http", alternatively execute the below examples:

#### create user 1

`
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username": "theBanker"}'
`

#### create user 2

`
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username": "theAccountant"}'
`

#### make a deposit

`
curl -X POST http://localhost:8080/accounts/deposit -H "Content-Type: application/json" -d '{"username": "theBanker","units": 50}'
`

#### make a withdrawal

`
curl -X POST http://localhost:8080/accounts/withdraw -H "Content-Type: application/json" -d '{"username": "theBanker","units": 25}'
`

#### make a transfer

`
curl -X POST http://localhost:8080/accounts/withdraw -H "Content-Type: application/json" -d '{"fromUser": "theBanker","toUser": "theAccountant","units": 10}}'
`

### get balance

`
curl -X GET http://localhost:8080/accounts/balances/theBanker
`

### get transaction history

`
curl -X GET http://localhost:8080/accounts/theBanker/transactions
`
