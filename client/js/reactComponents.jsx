class Login extends React.Component {
	constructor(props){
		super(props);
		this.state = {username:""};
		this.changeHandler = this.changeHandler.bind(this);
		this.submitHandler = this.submitHandler.bind(this);
	}

	changeHandler(event){
		this.setState({username:event.target.value});
	}
	submitHandler(event){
		event.preventDefault();
		let username = this.refs.usernameField.value;
		let password = this.refs.passwordField.value;
		this.setState({submitted:true, username:this.state.username});

		LoginManager.login(username, password, function(data){});
		application.setView(1);
	}

	render() {
		if(this.state.submitted){
			return (
				<div>Logged in as {this.state.username}</div>
			);
		}
		return (
			<form className="login-form" onSubmit = {this.submitHandler}>
				<h1>Login</h1>
				Username:<br />
				<input ref="usernameField" onChange={this.changeHandler}type="text" name="username" placeholder="User@kth.se" required /><br />
				Password:<br />
				<input ref="passwordField" type="password" name="password" placeholder="Pass..." required /><br />
				<input type="submit" value="Submit" />
			</form>
		);
	}
}

Login.defaultProps = {
    username: "User",
    submitted: false
};

class ChatListEntry extends React.Component {
	constructor(props) {
		super(props);
		this.onClick = this.onClick.bind(this);
	}

	onClick (){
		this.props.setChatId(this.props.id);
	}

	render() {
		return (
			<div onClick={this.onClick}> {dataManager.chats[this.props.id].title} (id {this.props.id}) </div>
		);
	}
}

class AddChat extends React.Component {
	constructor(props) {
		super(props);
		this.submitHandler = this.submitHandler.bind(this);
	}

	submitHandler(event){
		event.preventDefault();
		let newChatName = this.refs.chatnameField.value;
		postManager.addChat(newChatName, [], 
			function(data){
				forceUpdateAll();
			});
	}

	render() {
		return (
			<div>
			<form className="newchat-form" onSubmit = {this.submitHandler}>
				<input ref="chatnameField" type="text" placeholder="Chat name" required /><br />
				<input type="submit" value="Create new chat" />
			</form>
			<br/>
			<a href="#" onClick={() => this.props.setView(0)}> Back </a>
			</div>
		);
	}
}

class ChatList extends React.Component {
	constructor(props) {
		super(props);
		this.state = {view: 0};
		this.setView = this.setView.bind(this);
	}

	setView(newView){
		this.setState({
			view: newView
		});
	}

	render() {
		if (this.state.view == 1) {
			return (
				<AddChat setView={this.setView}/>
			);
		}

		var rows = [];
		for (var chatId in dataManager.chats) {
			var chat = dataManager.chats[chatId];
			rows.push(<ChatListEntry key={chatId} id={chatId} setChatId={this.props.setChatId}/>);
		}

		return (
			<div>
			{rows}
			<a href="#" onClick={() => this.setView(1)}> Add chat </a>
			</div>
		);
	}
}

class ChatMessage extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		var username = dataManager.users[this.props.data.sender].username;
		return (
			<div className="message">
				<span className="username">{username}<br/></span>
				{this.props.data.message}
			</div>
		);
	}
}

class ChatView extends React.Component {
	constructor(props) {
		super(props);
		this.sendMessage = this.sendMessage.bind(this);
	}

	sendMessage(event){
		event.preventDefault();
		postManager.addMessage(this.props.chatId, this.refs.messageField.value, 
			function(data){
				forceUpdateAll();
			});
		this.refs.messageField.value = "";
	}

	render() {
		if (!(this.props.chatId in dataManager.chats))
			return <div> <h3>No chat chosen ({this.props.chatId})</h3> </div>;

		var chat = dataManager.chats[this.props.chatId];

		var messages = [];
		for (let message of chat.messages) {
			messages.push(<ChatMessage key={message.id} data={message}/>);
		}


		return (
			<div>
				<h3>{dataManager.chats[this.props.chatId].title}</h3>

				{messages}
				
				<br/>
				Write message:
				<form id="messageForm" onSubmit = {this.sendMessage}>
					<input type="text" ref="messageField" className = "messagefield"/>
				</form>
			</div>
		);
	}
}

class Application extends React.Component {
	constructor(props) {
		super(props);

		this.state = {chatId: -1, 
			view: LoginManager.isLoggedIn() ? 1 : 0
		};

		this.setChatId = this.setChatId.bind(this);
		this.logout = this.logout.bind(this);
		this.setView = this.setView.bind(this);
	}

	setChatId(newId){
		this.setState({chatId: newId});
	}

	logout(){
		LoginManager.logout();
		this.setView(0);
	}

	setView(newView){
		this.setState({view: newView});
	}

	render() {
		if (this.state.view == 0){
			return (
				<div>
					<Login/>
				</div>
			);
		}

		if (this.state.view == 2){
			return (
				<div>
					<AddChat/>
				</div>
			);
		}

		return (
			<div>
				<span>Logged in as {LoginManager.username} </span>
				 <a href="#" onClick={this.logout}>Logout</a>
				<div>
					<div className="chatlist">
						<h3>Chats</h3>
						<ChatList id="chatList" setChatId={this.setChatId}/>
					</div>

					<div className="chatview">
						<ChatView id="chatView" chatId={this.state.chatId}/>
					</div>
				</div>
			</div>
		);
	}
}

// This should not be used once we get notifications from backend.
function forceUpdateAll(){
	loadInitialData(dataManager, apiAddress, function(){
		application.forceUpdate();
	});
}

var application = ReactDOM.render(
	<Application />,
	document.getElementById('application')
);

loadInitialData(dataManager, apiAddress, function(){
	application.forceUpdate();
});

document.getElementById("datadump").innerHTML = JSON.stringify(dataManager, null, 2);