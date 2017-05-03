// Holds all data
function DataManager() {
    this.chats = {};
    this.users = {};
}
// Add a chat, if it doesn't already exist.
DataManager.prototype.putChat = function(chatObject) {
    if (chatObject.id in this.chats)
        return;
    this.chats[chatObject.id] = new ChatData(chatObject);
}
// Add a message to the chat with id chatId
DataManager.prototype.putMessage = function(chatId, messageObject) {
    this.chats[chatId].putMessage(messageObject);
}


// Holds data for a specific Chat
function ChatData(chatObject) {
    Object.assign(this, chatObject); //Merge the data from the object (title, id etc) into this one
    
    this.messages = []; //List of message objects
}
// Add a message to the chat
ChatData.prototype.putMessage = function(messageObject){
    this.messages.push(new MessageData(messageObject));
}

// Holds data for a specific message.
function MessageData(messageObject) {
    Object.assign(this, messageObject);
    self.timeSent = new Date();
}