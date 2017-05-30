function loadInitialData(dataManager, apiAddress, callback){
	// Add chats
	$.get(apiAddress+"/chat", function(data){
		var promises = [];

		var chats = data;
		for (let chat of chats){
			dataManager.putChat(chat);

			for (let user of chat.participants){
				dataManager.putUser(user);
			}
		}

		var addMessageClosure = function(chatId){
			return function(data){
				var messages = data;

				for (let message of messages)
					dataManager.putMessage(chatId, message);
			}
		}

		// Add messages to chats
		for (var chatId in dataManager.chats){
			promises.push($.get(apiAddress+"/chat/"+chatId+"/message", addMessageClosure(chatId)));
		}

		$.when.apply($, promises).then(function() {
			if (callback != null)
				callback();
		}, function() {console.log("error")});
	});
}