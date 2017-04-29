# sjchat

[![Build Status](https://travis-ci.org/sjchat/sjchat.svg?branch=master)](https://travis-ci.org/sjchat/sjchat)

## Backend

### Building
``sh build.sh``

### Running services
``sh run.sh``


## Docker

1. Build with `sh build.sh`
2. Build docker images with:

```
docker build -t sjchat-message-service ./message-service
docker build -t sjchat-user-service ./user-service
docker build -t sjchat-restapi ./restapi
```
3. Run all services with `docker deploy stack -c docker-compose.yaml sjchat`
4. You can now access the services via `localhost:8080` etc.