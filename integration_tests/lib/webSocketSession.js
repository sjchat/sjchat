/**
 * Created by jovi on 2017-06-20.
 */
'use strict';
var WebSocket = require('ws');
var sleep = require('sleep');

module.exports = {
  connect: connect,
  send: send,
  waitForMessage: waitForMessage
};

/**
 * Call in context of a logged in REST session
 *
 */
function connect(callback){
  var self = this;
  this.ws = new WebSocket('ws://localhost:8443');
  self.lastWSMessage = '';
  this.ws.on('message', function(data){
    self.lastWSMessage = data;
  });
  this.ws.on('open', callback);
}

function send(message, callback){
  this.ws.send(message, function(){
    sleep.msleep(400); //Hold to allow the server to send a response
    callback();
  });
}

function waitForMessage(needle, callback){
  var self = this;
  this.ws.on('message', listener);

  function listener(data){
    if(data.indexOf(needle) !== -1){
      self.ws.removeEventListener(listener);
      callback();
    }
  }
}