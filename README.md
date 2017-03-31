# sjchat

[![Build Status](https://travis-ci.org/sjchat/sjchat.svg?branch=master)](https://travis-ci.org/sjchat/sjchat)

## Backend
### Starting servers
(First setup the project by running maven)
- Start the REST API server located in src/main/java/sjchat/backend/restapi/Application.java. The REST API runs on http://localhost:8080.
- Start the message service server located in src/main/java/sjchat/backend/messages/MessageServiceServer.java. The message service runs on port 50052.
- Start the user service server located in src/main/java/sjchat/backend/users/UserServiceServer.java The user service runs on port 50051.
