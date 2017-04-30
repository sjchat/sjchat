# Create managers
docker-machine create sjchat-staging-mgr1 -d google --google-zone europe-west1-c --google-tags sjchat-staging --google-tags manager --google-machine-type f1-micro --google-project inda-swag

# docker swarm init
# The command above will print the commands used for initing the swarm
# and the command for workers to join the manager.


# Create workers
docker-machine create sjchat-staging-w1 -d google --google-zone europe-west1-c --google-tags sjchat-staging --google-tags worker --google-machine-type f1-micro --google-project inda-swag
docker-machine create sjchat-staging-w2 -d google --google-zone europe-west1-c --google-tags sjchat-staging --google-tags worker --google-machine-type f1-micro --google-project inda-swag

# docker swarm join

# Open ports
gcloud compute firewall-rules create restapi --allow tcp:8080 --target-tags manager
gcloud compute firewall-rules create visualizer --allow tcp:8081 --target-tags manager
gcloud compute firewall-rules create web-client --allow tcp:8082 --target-tags manager

# SCP the docker-compose file
docker-machine scp docker-compose-staging.yaml sjchat-staging-mgr1:~

# Start the cluster
docker-machine ssh sjchat-staging-mgr1 "sudo docker stack deploy -c docker-compose-staging.yaml sjchat"