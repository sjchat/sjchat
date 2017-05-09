#!/usr/bin/env bash
curl https://raw.githubusercontent.com/sjchat/sjchat/master/docker-compose-staging.yaml > docker-compose-staging.yaml
sudo docker stack deploy -c docker-compose-staging.yaml sjchat
