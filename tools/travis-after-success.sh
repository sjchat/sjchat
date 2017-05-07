if [ "$TRAVIS_BRANCH" == "master" ]
then
    docker login -e $DOCKER_EMAIL -u $DOCKER_USER -p $DOCKER_PASS

    docker build -t sjchat-message-service ./message-service
    docker build -t sjchat-user-service ./user-service
    docker build -t sjchat-restapi ./restapi
    docker build -t sjchat-web-client ./client

    ./tools/publish-docker-image.sh
fi