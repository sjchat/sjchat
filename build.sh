set -e
echo "Build started"

echo "Building queue project"
mvn -f queue install


# Docker

echo "Building docker container 'sjchat-message-service'"
docker build -t sjchat-message-service ./message-service

echo "Building docker container 'sjchat-user-service'"
docker build -t sjchat-user-service ./user-service

echo "Building docker container 'sjchat-restapi'"
docker build -t sjchat-restapi ./restapi

echo "Building docker container 'sjchat-web-client'"
docker build -t sjchat-web-client ./client

echo "Build finished"
