set -e
echo "Build started"

echo "Building database service"
mvn -f database-service install

echo "Building service general"
mvn -f service-general install

echo "Building user service"
mvn -f user-service install

echo "Building message service"
mvn -f message-service install

echo "Building queue project"
mvn -f queue install

echo "Building rest api"
mvn -f restapi install



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
