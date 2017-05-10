class PostManager {
        
    constructor(myUrl) {
        this.url = myUrl;
    }
    
    postData(inUrl, inData, callback) {
        $.ajax({
            url: this.url+"/"+inUrl,
            type: "POST",
            data: JSON.stringify(inData),
            contentType: "application/json",
            dataType: "json",
            success: function(data, status){
                if (status != "success") {
                    console.log("data not sent due to " + status);
                } else {
                    if (callback != null)
                        callback(data);
                }
            }
        });
    }
    
    addMessage(chatId, myMessage, callback) {
        
        this.postData("chat/" + chatId +"/message", {
            "message": myMessage,
            "sender": 1
        }, callback);
    }

    addUser(myUsername, callback) {

        this.postData("user", {
            "username": myUsername
        }, callback);
    }

    addChat(myTitle, myParticipants, callback) {

        this.postData("chat", {
            "title": myTitle,
            "participants": myParticipants
        }, callback);
    }
}
