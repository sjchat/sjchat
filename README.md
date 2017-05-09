# sjchat

[![Build Status](https://travis-ci.org/sjchat/sjchat.svg?branch=master)](https://travis-ci.org/sjchat/sjchat)


## General

 - Production server: http://sjchat.wallstrom.it:8082/client.html
 - Staging/dev server: http://staging.sjchat.wallstrom.it:8082/client.html

 - RestAPI is located at http://sjchat.wallstrom.it:8080/ (See [swaggerhub](https://app.swaggerhub.com/apis/alanihre/SJCHAT/0.1.2) for API spec or `rest-api-0_1_2.swagger.yaml`)
 - Server cluster visualizer exist at http://sjchat.wallstrom.it:8081/
 - Web server / web client exist at http://sjchat.wallstrom.it:8082/client.html
 - For other services ports, check the `docker-compose-production.yaml` file.

## What do I need to get started?

 - Java
 - Maven
 - Docker

### Install build tools
You need both **maven** and **docker** to compile and run the project.
1. Google how to install maven, this depends on your OS.
2. To install docker go [here](https://docs.docker.com/). If you run linux the instructions are under "Install using the repository".

### Extra steps for linux
3. If the build fails with "Got permission denied when trying to connect...", you need to add yourself to the docker group. This is done with ``sudo usermod -a -G docker [YOUR USERNAME]``. Logout/reboot afterwards.


## How do I compile and run this?

### Local development

1. Build with `sh build.sh`
2. Run all services with `run.sh`
3. You can now access the services via `localhost:8080` etc.
4. If you want to stop developing and waste your time on other schoolwork, run `docker stack rm sjchat`.

### Manually pushing to staging/dev

1. Build with `sh build.sh`
2. Upload all images to the public docker hub with `sh tools/publish-docker-images.sh` (You will need to join the organisation sjchat on docker hub to get access)
3. Upload the latest docker-compose file to the manager server with `docker-machine scp docker-compose-staging.yaml sjchat-staging-mgr1:~` or `gcloud compute copy-files docker-compose-staging.yaml sjchat-staging-mgr1:/home/docker-user/`
4. SSH to the manager server in the docker swarm and run 
```
docker-machine ssh sjchat-staging-mgr1 "sudo docker stack deploy -c docker-compose-staging.yaml sjchat"
```
This command probably create some downtime.

### Updating staging deployment script
1. Upload scripts to server with `gcloud compute copy-files tools/deploy-service.py tools/deploy-script.sh tools/start-deploy-service.sh root@sjchat-staging-mgr1:/home/docker-user/auto-deploy/`. Select zone 10 (europe-west-c)
2. SSH in with `gcloud compute ssh sjchat-staging-mgr1`
3. Move to correct folder with `cd /home/docker-user/auto-deploy/`
4. Kill the old script with `sudo` and `jobs`/`ps`/`kill`
5. Start the new script with `sudo sh run-deploy-service.sh`

### It doesn't work! :c

If you're stuck ask on slack for help, and update this with what went wrong and how you solved it!

#### ``docker stack deploy`` scares me with ``unable to pin image`` and ``unauthorized: authentication required``
This is normal, and just a warning that the images wasn't found online so docker uses the local ones. Everything actually works

#### I have run out of disk space!
Old docker images are saved on your system. **All docker images on your system** can be removed by running cleanup-docker.sh in tools.
