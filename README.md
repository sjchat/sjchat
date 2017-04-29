# sjchat

[![Build Status](https://travis-ci.org/sjchat/sjchat.svg?branch=master)](https://travis-ci.org/sjchat/sjchat)

## Backend

### Building
``sh build.sh``

### Running services
``sh run.sh``


## Docker

### Local development

1. Build with `sh build.sh`
2. Run all services with `docker deploy stack -c docker-compose.yaml sjchat`
3. You can now access the services via `localhost:8080` etc.

### Pushing to production

1. Build with `sh build.sh`
2. Upload all images to the public docker hub with `sh tools/publish-docker-images.sh` (Only sawny can do this atm as everything is setup on his account)
3. SSH to the manager server in the docker swarm and run 
```
docker-machine ssh manager-1 "sudo docker stack rm sjchat"
docker-machine ssh manager-1 "sudo docker stack deploy -c docker-compose-staging.yaml sjchat"
```

This will cause a few seconds of downtime though. To prevent downtime you need to run two instances of every service and then take down one of the services, upload a newer version, take down the second service, upload the new service.
