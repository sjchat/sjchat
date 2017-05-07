// Holds all data
class DataManager {
    constructor() {
        this.chats = {};
        this.users = {};
    }

    // Add a chat, if it doesn't already exist.

    putChat(chatObject) {
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
        this.chats[chatId].putMessage(messageObject);
    }

}


class ChatData {
    constructor(chatObject) {
        Object.assign(this, chatObject); //Merge the data from the object (title, id etc) into this one
        this.messages = []; //List of message objects
    }

    putMessage(messageObject) {
        this.messages.push(new MessageData(messageObject));
    }

}

class MessageData {
    constructor(messageObject) {
        Object.assign(this, messageObject);
        self.timeSent = new Date();
    }

}



