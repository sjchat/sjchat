class NetworkManager {
	constructor() {
		this.socket = new WebSocket('ws://localhost:8080');

		this.socket.addEventListener('message', function(event) {
			dataManager.putMessage(event.data);
		});

		this.socket.addEventListener('connect', function(event) {
			console.log('Connected to server', event.data);
		})
	}
}