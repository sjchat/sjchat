'use strict';
module.exports = function(){
  /**
   * Matches the last statuscode we got.
   *
   * @param {Number}    statusCode    The status code the last request should've had
   * @param {Function}  callback      Called with an error if there is one
   * */
  this.Then(/^the response has status code (\d\d\d)$/, function(status, callback){
    var correctStatus = status == this.restSessions[this.lastUser].lastResponseStatus;
    if(correctStatus){
      return callback();
    }
    callback(new Error('Not correct status, should have been: ' + status + ', was: ' + this.restSessions[this.lastUser].lastResponseStatus));
  });

  /**
   * Checks that the last rest request included this property and value
   *
   * @param {String}    property    Property to check
   * @param {String}    value       Value the property should have
   * @param {Function}  callback    Called with an error if there is one
   * */
  this.Then(/^the response includes property "([^"]*)" with value "([^"]*)"$/, function(property, value, callback){
    var object = this.restSessions[this.lastUser].lastResponseBody;
    if(object[property] == value){
      return callback();
    }
    return callback(new Error('Property: ' + property + ' with value: ' + value + ', not included in: ' + JSON.stringify(this.restSessions[this.lastUser].lastResponseBody)));
  });

  /**
   * Checks that the last rest request included this property and value
   *
   * @param {String}    includePhrase   Whether or not it should include said property
   * @param {String}    property        Property to check
   * @param {Function}  callback        Called with an error if there is one
   * */
  this.Then(/^the response (includes|does NOT include) property "([^"]*)"$/, function(includePhrase, property, callback){
    var errMsg = includePhrase === 'includes' ? 'not included' : 'included';
    var object = this.restSessions[this.lastUser].lastResponseBody;
    if(includePhrase === 'includes' && object[property] != undefined){
      return callback();
    }

    if(includePhrase === 'does NOT include' && object[property] == undefined){
      return callback();
    }

    return callback(new Error('Property: ' + property + ', ' + errMsg + ' in: ' + JSON.stringify(this.restSessions[this.lastUser].lastResponseBody)));
  });
};