/**
 * Created by jovi on 2017-06-20.
 */
'use strict';

var webSocket = require('../lib/webSocketSession.js');

module.exports = function(){
  this.Given(/^a websocket session is started for user "([^"]*)"( without enrolling)?$/, function(username, enrollPhrase, callback){
    var session = this.restSessions[username];
    webSocket.connect.call(session, function(){
      if(!enrollPhrase) {
        return webSocket.send.call(session, JSON.stringify({path: '/enroll', token: session.token}), function(){
          webSocket.waitForMessage.call(session, 'Subscribed to messages to:', callback);
        });
      }
      callback();
    });
  });

  this.Then(/^there is (a|no) message from the websocket for user "([^"]*)" that says: "([^"]*)"$/, function(oneOrNone, username, message, callback){
    var lastMessage = this.restSessions[username].lastWSMessage;
    if(oneOrNone === 'no' ){
      if(lastMessage.indexOf(message) === -1){
        return callback();
      }
      return callback(new Error('There is a message to user: ' + username + ' containing: ' + message + ', lastMessage: ' + lastMessage));
    }
    if(lastMessage.indexOf(message) !== -1){
      return callback();
    }
    webSocket.waitForMessage.call(this.restSessions[username], message, callback);
  });

  this.When(/^I send a message "([^"]*)" from user "([^"]*)" to user "([^"]*)"$/, function(message, fromUser, toUser, callback){
    var toId = JSON.parse(Buffer.from(this.restSessions[toUser].token.split('.')[1], 'base64')).sub;
    webSocket.send.call(this.restSessions[fromUser], JSON.stringify({path: '/send', message: message, receiverId: toId}), callback);
  });
};