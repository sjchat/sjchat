set -e
echo "Build started"

echo "Building user service"
mvn -f user-service install

echo "Building message service"
mvn -f message-service install

echo "Building rest api"
mvn -f restapi install

echo "Building docker container 'sjchat-message-service'"
docker build -t sjchat-message-service ./message-service

echo "Building docker container 'sjchat-user-service'"
docker build -t sjchat-user-service ./user-service

echo "Building docker container 'sjchat-restapi'"
docker build -t sjchat-restapi ./restapi

echo "Building docker container 'sjchat-web-client'"
docker build -t sjchat-web-client ./client

echo "Build finished"
