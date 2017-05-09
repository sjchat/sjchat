function loadInitialData(dataManager, apiAddress){
	// Add chats
	$.get(apiAddress+"/chat", function(data){
		var chats = data;

		for (let chat of chats){
			dataManager.putChat(chat);

			for (let user of chat.participants){
				dataManager.putUser(user);
			}
		}
	});

	// Add messages to chats
	for (var chatId in dataManager.chats){
		$.get(apiAddress+"/chat/"+chatId+"/message", function(data){
			var messages = data;

			for (let message of messages){
				dataManager.putMessage(chatId, message);
			}
		});
	}
}