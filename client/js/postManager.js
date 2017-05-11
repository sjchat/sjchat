class PostManager {
        
    constructor(myUrl) {
        this.url = myUrl;
    }
    
    postData(inUrl, inData) {
        
        var outData = undefined;
        
        $.ajax({
            url: inUrl,
            type: "POST",
            data: JSON.stringify(inData),
            contentType: "application/json",
            dataType: "json",
            success: function(data, status){
                if (status != "success") {
                    console.log("data not sent due to " + status);
                } else {
                    outData = data;
                }
            }
        });
        
        return outData;
    }
    
    addMessage(chatId, myMessage) {
        
        var messageData = this.postData(this.url + "/" + chatId +"/message", {
                "message": myMessage
            });
        
        if(messageData != undefined) return messageData.id;
    }

    addUser(myUsername) {
        
        var userData = this.postData(this.url + "/user", {
                "username": myUsername
            });
        
        if(userData != undefined) return userData.id;
    }

    addChat(myTitle, myParticipants) {
            
        var chatData = this.postData(this.url + "/chat", {
                "title": myTitle,
                "participants": myParticipants
            });
        
        if(chatData != undefined) return chatData.id;
    }
}