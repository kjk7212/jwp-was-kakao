package service;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class ApiService {

	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String USER_ID = "userId";
	public static final String PASSWORD1 = "password";
	public static final String REDIRECT_URL_INDEX = "/index.html";
	public static final String REDIRECT_URL_LOGIN_FAILED = "/user/login_failed.html";

	public HttpResponse redirectTo(String redirectUrl) {
		return HttpResponse.movedPermanently(redirectUrl);
	}

	public HttpResponse saveUserFromBody(HttpRequest httpRequest) {
		String name = httpRequest.getFromBody(NAME);
		String email = httpRequest.getFromBody(EMAIL);
		String password = httpRequest.getFromBody(PASSWORD);
		String userId = httpRequest.getFromBody(USER_ID);

		if (name == null || email == null || password == null || userId == null) {
			return HttpResponse.badRequest("필요한 파라미터가 존재하지 않습니다.");
		}

		if (DataBase.findUserById(userId) != null) {
			return HttpResponse.conflict("이미 존재하는 유저입니다.");
		}
		DataBase.addUser(new User(userId, password, name, email));

		return HttpResponse.found(REDIRECT_URL_INDEX);
	}

	public HttpResponse login(HttpRequest httpRequest) {
		String userId = httpRequest.getFromBody(USER_ID);
		String password = httpRequest.getFromBody(PASSWORD1);

		User user = DataBase.findUserById(userId);
		if (user == null || !user.getPassword().equals(password)) {
			return HttpResponse.found(REDIRECT_URL_LOGIN_FAILED);
		}

		return HttpResponse.found(REDIRECT_URL_INDEX).setCookieLogined(userId);
	}
}
