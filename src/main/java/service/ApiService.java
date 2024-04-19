package service;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class ApiService {
	public HttpResponse redirectTo(String redirectUrl) {
		return HttpResponse.movedPermanently(redirectUrl);
	}

	public HttpResponse saveUserFromBody(HttpRequest httpRequest) {
		String name = httpRequest.getFromBody("name");
		String email = httpRequest.getFromBody("email");
		String password = httpRequest.getFromBody("password");
		String userId = httpRequest.getFromBody("userId");

		if (name == null || email == null || password == null || userId == null) {
			return HttpResponse.badRequest("필요한 파라미터가 존재하지 않습니다.");
		}

		if (DataBase.findUserById(userId) != null) {
			return HttpResponse.conflict("이미 존재하는 유저입니다.");
		}
		DataBase.addUser(new User(userId, password, name, email));

		return HttpResponse.found("/index.html");
	}

	public HttpResponse login(HttpRequest httpRequest) {
		String userId = httpRequest.getFromBody("userId");
		String password = httpRequest.getFromBody("password");

		User user = DataBase.findUserById(userId);
		if (user == null || !user.getPassword().equals(password)) {
			return HttpResponse.found("/user/login_failed.html");
		}

		return HttpResponse.found("/index.html").setCookieLogined(userId);
	}
}
