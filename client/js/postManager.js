class PostManager {
        
    constructor(myUrl) {
        this.url = myUrl;
    }
    
    addMessage(chatid, myMessage) {
        
        var id = -1;
        
        $.ajax({
            url: this.url + "/" + chatid +"/message",
            type: "POST",
            data: JSON.stringify({
                "message": myMessage
            }),
            contentType: "application/json",
            dataType: "json",
            success: function(data, status){
                if (status != "success") {
                    console.log("message not sent due to " + status);
                } else {
                    id = data.id;
                }
            }
        });
        
        return id;
    }

    addUser(myUsername) {
        
        var id = -1;
        
        $.ajax({
            url: this.url + "/user",
            type: "POST",
            data: JSON.stringify({
                "username": myUsername
            }),
            contentType: "application/json",
            dataType: "json",
            success: function(data, status){
                if (status != "success") {
                    console.log("user not sent due to " + status);
                } else {
                    id = data.id;
                }
            }
        });
        
        return id;
    }

    addChat(myTitle, myParticipants) {
    
        var id = -1;
        
        $.ajax({
            url: this.url + "/chat",
            type: "POST",
            data: JSON.stringify({
                "title": myTitle,
                "participants": myUsers
            }),
            contentType: "application/json",
            dataType: "json",
            success: function(data, status){
                if (status != "success") {
                    console.log("chat not sent due to " + status);
                } else {
                    var id = data.id;
                }
            }
        });
        
        return id;
    }
}
