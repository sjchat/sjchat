'use strict';
var MongoClient = require('mongodb').MongoClient;
module.exports = function(){
  this.Before(function(scenario, callback){
    this.restSessions = [];
    this.chatSessions = [];
    callback();
  });

  /**
   * Resets the server state
   * */
  this.Before(function(scenario, callback){
    MongoClient.connect('mongodb://localhost/webserver', function(err, db){
      if(err){
        return callback(err);
      }
      //db.dropDatabase();
      db.collection('user', {}, function(err, col){
        col.deleteMany({}, function(){
          callback();
        });
      });

    })
  });
}
