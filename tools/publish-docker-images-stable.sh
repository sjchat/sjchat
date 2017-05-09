docker tag sjchat-web-client sjchat/sjchat-web-client:stable
docker push sjchat/sjchat-web-client:stable

docker tag sjchat-message-service sjchat/sjchat-message-service:stable
docker push sjchat/sjchat-message-service:stable

docker tag sjchat-restapi sjchat/sjchat-restapi:stable
docker push sjchat/sjchat-restapi:stable

docker tag sjchat-user-service sjchat/sjchat-user-service:stable
docker push sjchat/sjchat-user-service:stable