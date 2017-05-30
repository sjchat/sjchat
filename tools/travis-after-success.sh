if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]
then
    docker login -u $DOCKER_USER -p $DOCKER_PASS

    docker build -t sjchat-message-service ./message-service
    docker build -t sjchat-user-service ./user-service
    docker build -t sjchat-restapi ./restapi
    docker build -t sjchat-web-client ./client

    ./tools/publish-docker-images.sh
    curl -s http://staging.sjchat.wallstrom.it:50053/deploy$DEPLOY_CODE
fi
