class PostManager {
        
    constructor(myUrl) {
        this.url = myUrl;
    }
    
    addMessage(chatid, myMessage) {
        
        $.post(this.url + "/" + chatid +"/message", JSON.stringify({
            "message": myMessage
        }),
        function(data, status) {
            if (status != "success") {
                console.log("message not sent due to " + status);
                return -1;
            } else {
                var value = JSON.parse(data);
                return value.id;
            }
	   });
    }

    addUser(myUsername) {
	
        $.post(this.url + "/user", JSON.stringify({
            username: myUsername
        }),
        function(data, status) {
            if(status != "success") {
                console.log("user not sent due to " + status);
                return -1;
            } else {
                var value = JSON.parse(data);
                return value.id;
            }
        });
    }

    addChat(myTitle, myUsers) {
	
        $.post(this.url + "/user", JSON.stringify({
            "title": myTitle,
            "users": myUsers
        }),
        function(data, status) {
            if(status != "success") {
                console.log("chat not sent due to " + status);
            } else {
                var value = JSON.parse(data);
                return -1;
                return value.id;
            }
        });
    }
}
