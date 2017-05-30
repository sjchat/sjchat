// Holds all data
class DataManager {
    constructor() {
        this.chats = {};
        this.users = {};
    }

    // Add a chat, if it doesn't already exist.

    putChat(chatObject) {
        //Put all the users
        for (let user of chatObject.participants)
            this.putUser(user);

        if (chatObject.id in this.chats)
            return;
        this.chats[chatObject.id] = new ChatData(chatObject);
    }

    // Add a user, if it doesn't already exist.
    putUser(userObject) {
        if (userObject.id in this.users)
            return;
        this.users[userObject.id] = userObject;
    }

    // Add a message to the chat with id chatId
    putMessage(chatId, messageObject) {
        // If the message is sent by an undefined user, add the user.
        // If the user is already defined this object will not be added.
        this.putUser({
            "id": messageObject.sender,
            "username": "unnamed_user_"+messageObject.sender
        });

        this.chats[chatId].putMessage(messageObject);
    }

    addDebugChats(){
        this.putChat({
            id: 1001, 
            title: "Mock Chat A", 
            participants: [
                {
                    id: 1,
                    username: "bulbasaur"
                },
                {
                    id: 2,
                    username: "ivysaur"
                },
                {
                    id: 3,
                    username: "venusaur"
                }
            ]
        });

        this.putChat({
            id: 1002, 
            title: "Mock Chat B", 
            participants: [
                {
                    id: 4,
                    username: "charmander"
                },
                {
                    id: 5,
                    username: "charmeleon"
                },
                {
                    id: 6,
                    username: "charizard"
                }
            ]
        });

        for (let i = 1; i <= 15; ++i)
            this.putMessage(Math.floor(Math.random()*2)+1001, {
                id: i,
                message: Math.random(),
                sender: Math.floor(Math.random()*6)+1
            });
    }
}


class ChatData {
    constructor(chatObject) {
        Object.assign(this, chatObject); //Merge the data from the object (title, id etc) into this one
        this.messages = []; //List of message objects
        this.messageIds = new Set();
    }

    putMessage(messageObject) {
        // Don't add duplicate messages
        if (this.messageIds.has(messageObject.id))
            return;
        this.messageIds.add(messageObject.id);

        this.messages.push(new MessageData(messageObject));
    }

}

class MessageData {
    constructor(messageObject) {
        Object.assign(this, messageObject);
        self.timeSent = new Date();
    }

}



