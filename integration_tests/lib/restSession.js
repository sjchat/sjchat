'use strict';
var Client = require('node-rest-client').Client;
var request = require('request');
var client = new Client({
  //'application/json; charset=utf-8',
  mimetypes: {
    json: ['application/json', 'application/json;charset=UTF-8'],
    //xml: ['application/xml', 'application/xml; charset=utf-8']
  }
});

const url = "http://localhost:8080";

module.exports = {
  post: post,
  get: get,
};
/**
 * Do a rest POST request. Call it with the context of the use (.call(this["user"])
 *
 * @param {String}    path      Path of the post request
 * @param {Object}    body      Body of the post request
 * @param {Function]  callback  Called with an error if there is one
 * @param {Object}    options   Optional options to pass in
 * */
function post(path, body, callback, options){
  var self = this;
  var args = {
    data: body,
    headers: { "Content-Type": "application/json" }
  };

  var options = {
    method: 'POST',
    url: toUrl(path),
    headers: getHeaders.call(this),
    body: JSON.stringify(body),
  }
  request(options, function(err, response, body){
    self.lastResponseBody = JSON.parse(response.body);
    self.lastResponseStatus = response.statusCode;
    callback(err, self.lastResponseBody, self.lastResponseStatus);
  });
}

/**
 * Do a rest GET request. Call it with the context of the use (.call(this["user"])
 *
 * @param {String}    path      Path of the post request
 * @param {Function]  callback  Called with an error if there is one
 * @param {Object}    options   Optional options to pass in
 * */
function get(path, callback, options){
  var self = this;
  var options = {
    method: 'GET',
    url: toUrl(path),
    headers: getHeaders.call(this)
  };
  request(options, function(err, response, body){
    self.lastResponseBody = JSON.parse(response.body);
    self.lastResponseStatus = response.statusCode;
    callback(err, self.lastResponseBody, self.lastResponseStatus);
  });
}

function toUrl(path){
  return url + path;
}

/**
 * Get headers for a default request
 * Checks for any tokens
 * Call it with getHeaders.call(this)
 * */
function getHeaders(){
  var headers = {"Content-Type": "application/json"};
  if(this.token !== undefined){
    headers.Authentication = this.token;
  }
  return headers;
}