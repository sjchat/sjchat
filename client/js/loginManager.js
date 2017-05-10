
class LoginManager {

	// Logs in to server
	static login(username, password, callback){
		this.username = username;

		postManager.postData("login", {
			username: username,
			password: password
		}, function(data){
			let respones = data;
			let token = "fail";

			if (response.status == "success")
				token = response.token;

			token = "removeWhenLoginWorks";

			if (token != "fail")
				Cookies.set(this.cookieName, token);

			callback(data);
		});
	}

	static logout(){
		Cookies.remove(this.cookieName);
	}

	static getToken(){
		return Cookies.get(this.cookieName);
	}

	static isLoggedIn(){
		let token = this.getToken();
		return token != "fail";
	}
}

LoginManager.cookieName = 'token';
LoginManager.username = 'nobody';