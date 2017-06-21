/**
 * Created by jovi on 2017-06-17.
 */
'use strict';
var rest = require('./../lib/restSession.js');

module.exports = function(){
  /**
   * Creates a user with realName the same as username
   *
   * @param {String}    username    The username of the user
   * @param {String}    password    The password of the user
   * @param {Function}  callback    Called with an error if there is one
   * */
  this.Given(/^I create a user "([^"]*)" with password "([^"]*)"$/, function(username, password, callback){
    var body = {
      username: username,
      password: password,
      realName: username
    };

    this.restSessions[username] = {};
    rest.post.call(this.restSessions[username], '/user', body, callback);
    this.lastUser = username;
  });

  /**
   * Create a user without one property set
   *
   * @param {String}    propertyNotSet    The property that shouldn't be set
   * @param {Function}  callback          Called with an error if there is one
   * */
  this.Given(/^I create a user without property "([^"]*)"$/, function(propertyNotSet, callback){
    var body = {
      username: 'test',
      password: 'test',
      realName: 'test'
    };

    delete body[propertyNotSet];
    this.restSessions['test'] = {};
    rest.post.call(this.restSessions['test'], '/user', body, callback);
    this.lastUser = 'test';
  });

  /**
   * Creates user with password "pass" and realName username.
   * Also logs in the user
   *
   * @param {String}    username    The username of the user
   * @param {Function}  callback    Called with an error if there is one
   * */
  this.Given(/^user "([^"]*)" is created and logged in$/, function(username, callback){
    var body = {
      username: username,
      password: 'pass',
      realName: username
    };
    var self = this;
    this.restSessions[username] = {};
    rest.post.call(self.restSessions[username], '/user', body, function(){
      login.call(self.restSessions[username], {username: username, password: 'pass'}, true, callback);
      self.lastuser = username;
    });
  });

  /**
   * Log in user. (Gets token)
   *
   * @param {String}    username    Username of the user we want to log in
   * @param {String}    password    Password of the user we want to log in
   * @param {Function}  callback    Called with an error if there is one
   * */
  this.When(/^I log in user "([^"]*)" with password "([^"]*)"$/, function(username, password, callback){
    var body = {
      username: username,
      password: password
    };

    createRESTSessionIfNotExist.call(this, username);
    login.call(this.restSessions[username], body, false, callback);
    this.lastUser = username;
  });

/**
 * Check if the last session has a token
 *
 * @param {String}    haventPhrase    If set, it shouldn't have a token
 * @param {Function}  callback        Called with an error if there is one
 * */
  this.Then(/^I (don't )?have a token/, function(haventPhrase, callback){
    if(this.restSessions[this.lastUser].lastResponseBody.token === undefined && !haventPhrase){
      return callback(new Error('Token is undefined, state: ' + JSON.stringify(this.restSessions[this.lastUser])));
    }
    callback();
  });
};

/**
 * Logs in and sets token. Should be called in the context of a restSession.
 *
 * @param {Object}    body                Properties should be username and password
 * @param {Boolean}   crashOnWrongStatus  Should it crash if the status is not 200
 * @param {Function}  callback            Called with an error if there is one
 * */
function login(body, crashOnWrongStatus, callback){
  var self = this;
  rest.post.call(self, '/user/login', body, function(err, resBody, resStatus){
    if(resStatus != 200 && crashOnWrongStatus){
      return callback(new Error('Response status was not 200 was: ' + resStatus + ', response body: ' + JSON.stringify(resBody)));
    }
    self.token = resBody.token;
    callback();
  });
}

function createRESTSessionIfNotExist(username){
  if(this.restSessions[username] === undefined){
    this.restSessions[username] = {};
  }
}