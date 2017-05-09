class ChatListEntry extends React.Component {
	constructor(props) {
		super(props);
		this.changeChat = this.changeChat.bind(this);
	}

	changeChat (){
		chatView.setChatId(this.props.id);
	}

	render() {
		return (
			<div onClick={this.changeChat}> {dataManager.chats[this.props.id].title} (id {this.props.id}) </div>
		);
	}
}

class ChatList extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		var rows = [];
		for (var chatId in dataManager.chats) {
			var chat = dataManager.chats[chatId];
			rows.push(<ChatListEntry key={chatId} id = {chatId} />);
		}

		return (
			<div>
			{rows}
			</div>
		);
	}
}

class ChatMessage extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		var username = dataManager.users[this.props.data.user].username;
		return (
			<div style={{"backgroundColor": "rgba(255, 255, 255, 0.5)", "marginTop":"2px"}}>
				<span style={{"fontSize": "70%", "color":"white"}}>{username}<br/></span>
				{this.props.data.message}
			</div>
		);
	}
}

class ChatView extends React.Component {
	constructor(props) {
		super(props);
		this.state = {chatId: -1};
	}

	setChatId(newId){
		this.setState({chatId: newId});
	}

	render() {
		if (!(this.state.chatId in dataManager.chats))
			return <div> <h3>No chat chosen ({this.state.chatId})</h3> </div>;

		var chat = dataManager.chats[this.state.chatId];

		var messages = [];
		for (let message of chat.messages) {
			messages.push(<ChatMessage key={message.id} data={message}/>);
		}


		return (
			<div>
				<h3>{dataManager.chats[this.state.chatId].title}</h3>

				{messages}
				
				<br/>
				Write message:
				<input type="text" style={{"width": "100%"}}/>
			</div>
		);
	}
}

var chatView = ReactDOM.render(
	<ChatView />,
	document.getElementById('chatView')
);
ReactDOM.render(
	<ChatList />,
	document.getElementById('chatList')
);


document.getElementById("datadump").innerHTML = JSON.stringify(dataManager, null, 2);