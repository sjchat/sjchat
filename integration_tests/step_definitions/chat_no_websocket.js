'use strict';
var rest = require('./../lib/restSession.js');

module.exports = function(){
  this.Given(/^a chat "([^"]*)" is created$/, function(chatName, callback){
    this.chatSessions[chatName] = {};
    rest.post.call(this.chatSessions[chatName], '/chat', {title: chatName, participants: []}, callback);
  });

/**
* Send a message
*/
  this.Then(/^user "([^"]*)" sends message "([^"]*)" to chat "([^"]*)"$/, function(fromUser, message, chatName, callback){
    var session = this.restSessions[fromUser];
    getChatId(chatName, function(err, chatId){
      if(err){
        return callback(err);
      }
      rest.post.call(session, '/chat/' + chatId + '/message', {message: message}, callback);
    })
  });
}

function getChatId(chatName, callback){
  var state = {};
  rest.get.call(state, '/chat', function(){
    for(var i = 0; i < state.lastResponseBody.length; i++){
      if(state.lastResponseBody[i].title == chatName){
        return callback(undefined, state.lastResponseBody[i].id);
      }
    }
    callback(new Error('No chat with title: ' + chatName));
  });
}
