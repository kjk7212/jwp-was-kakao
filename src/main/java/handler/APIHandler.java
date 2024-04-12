package handler;

import static constant.Constant.*;

import annotations.APIMapping;
import db.DataBase;
import mapper.APIMapper;
import model.User;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Map;

public class APIHandler implements Handler {
	private static final APIMapper apiMapper = new APIMapper();

	public HttpResponse handle(HttpRequest httpRequest) {
		HttpMethod httpMethod = httpRequest.getHttpRequestLine().getHttpMethod();
		String path = httpRequest.getHttpRequestLine().getPath();

		if (apiMapper.notHasMapping(httpMethod, path)) {
			return HttpResponse.notFound(EMPTY_ERROR_MESSAGE);
		}

		Method method = apiMapper.getMethod(httpMethod, path);
		try {
			return (HttpResponse)method.invoke(this, httpRequest);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return HttpResponse.internalServerError(EMPTY_ERROR_MESSAGE);
		}
	}

	@APIMapping(path = "/", httpMethod = HttpMethod.GET)
	public HttpResponse redirectToIndex(HttpRequest httpRequest) {
		return HttpResponse.movedPermanently("/index.html");
	}

	@APIMapping(path = "/user/create", httpMethod = HttpMethod.POST)
	public HttpResponse saveUserFromBody(HttpRequest httpRequest) {
		Map<String, String> parameters = httpRequest.getHttpBody().getBody();

		String name = parameters.get("name");
		String email = parameters.get("email");
		String password = parameters.get("password");
		String userId = parameters.get("userId");

		if (name == null || email == null || password == null || userId == null) {
			return HttpResponse.badRequest("필요한 파라미터가 존재하지 않습니다.");
		}

		if (DataBase.findUserById(userId) != null) {
			return HttpResponse.conflict("이미 존재하는 유저입니다.");
		}
		DataBase.addUser(new User(userId, password, name, email));

		return HttpResponse.found("http://localhost:8080/index.html");
	}
}
