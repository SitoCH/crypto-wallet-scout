# (Crypto) wallet scout 

[![Java CI with Maven](https://github.com/SitoCH/crypto-wallet-scout/actions/workflows/ci.yml/badge.svg)](https://github.com/SitoCH/crypto-wallet-scout/actions/workflows/ci.yml) ![Docker Image Size (latest by date)](https://img.shields.io/docker/image-size/sito/crypto-wallet-scout) ![Docker Pulls](https://img.shields.io/docker/pulls/sito/crypto-wallet-scout)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SitoCH_crypto-wallet-scout&metric=coverage)](https://sonarcloud.io/summary/new_code?id=SitoCH_crypto-wallet-scout) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=SitoCH_crypto-wallet-scout&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=SitoCH_crypto-wallet-scout) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=SitoCH_crypto-wallet-scout&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=SitoCH_crypto-wallet-scout) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=SitoCH_crypto-wallet-scout&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=SitoCH_crypto-wallet-scout)  

This project uses Quarkus, the Supersonic Subatomic Java Framework.


## Prerequisites for local development

The following environment variables need to be set in a `.env` file in order to run the application locally:

```shell script
# Datasource
QUARKUS_DATASOURCE_USERNAME=
QUARKUS_DATASOURCE_PASSWORD=
QUARKUS_DATASOURCE_JDBC_URL=j

# OIDC
QUARKUS_OIDC_AUTH_SERVER_URL=
QUARKUS_OIDC_CLIENT_ID=
QUARKUS_OIDC_CREDENTIALS_SECRET=

# External API
BITQUERY_API_KEY=
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.


## Creating a Docker image

You can create a Docker image using:
```shell script
./mvnw clean package -Dquarkus.container-image.build=true
```

Or:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true

docker build -f src/main/docker/Dockerfile.native -t quarkus/crypto-wallet-scout .
```
