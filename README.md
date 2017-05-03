# sjchat

[![Build Status](https://travis-ci.org/sjchat/sjchat.svg?branch=master)](https://travis-ci.org/sjchat/sjchat)

## What do I need to get started?

### Install build tools
You need both maven and docker to compile and run the project.
1. Google how to install maven, this depends on your OS.
2. To install docker go [here](https://docs.docker.com/). If you run linux the instructions are under "Install using the repository.

### Extra steps for linux
3. If the build fails with "Got permission denied when trying to connect...", you need to add yourself to the docker group. This is done with ``sudo usermod -a -G docker [YOUR USERNAME]``. Logout/reboot afterwards.


## How do I compile and run this?

### Local development

1. Build with `sh build.sh`
2. Run all services with `docker stack deploy -c docker-compose-local.yaml sjchat`
3. You can now access the services via `localhost:8080` etc.
4. If you want to stop developing and waste your time on other schoolwork, run `docker stack rm sjchat`.

### Pushing to production

1. Build with `sh build.sh`
2. Upload all images to the public docker hub with `sh tools/publish-docker-images.sh` (Only sawny can do this atm as everything is setup on his account)
3. SSH to the manager server in the docker swarm and run 
```
docker-machine ssh manager-1 "sudo docker stack rm sjchat"
docker-machine ssh manager-1 "sudo docker stack deploy -c docker-compose-staging.yaml sjchat"
```

This will cause a few seconds of downtime though. To prevent downtime you need to run two instances of every service and then take down one of the services, upload a newer version, take down the second service, upload the new service.

## It doesn't work! :c

If you're stuck ask on slack for help, and update this with what went wrong and how you solved it!

### ``docker stack deploy`` scares me with ``unable to pin image`` and ``unauthorized: authentication required``
This is normal, and just a warning that the images wasn't found online so docker uses the local ones. Everything actually works