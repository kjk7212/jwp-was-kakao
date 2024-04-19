package handler;

import static constant.Constant.*;

import java.lang.reflect.Method;

import annotations.ApiMapping;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import mapper.ApiMapper;
import service.ApiService;

public class ApiHandler implements Handler {
	private static final String INDEX_URL = "/index.html";
	private static final String USER_LIST_URL = "/user/list.html";

	private static final ApiMapper apiMapper = new ApiMapper();
	public static final ApiService apiService = new ApiService();

	public HttpResponse handle(HttpRequest httpRequest) {
		HttpMethod httpMethod = httpRequest.getHttpMethod();
		String path = httpRequest.getPath();

		if (apiMapper.notHasMapping(httpMethod, path)) {
			return HttpResponse.notFound(EMPTY_ERROR_MESSAGE);
		}

		Method method = apiMapper.getMethod(httpMethod, path);
		try {
			return (HttpResponse)method.invoke(this, httpRequest);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return HttpResponse.internalServerError();
		}
	}

	@ApiMapping(path = "/", httpMethod = HttpMethod.GET)
	public HttpResponse redirectToIndex(HttpRequest httpRequest) {
		return apiService.redirectTo(INDEX_URL);
	}

	@ApiMapping(path = "/user/list", httpMethod = HttpMethod.GET)
	public HttpResponse redirectToUserList(HttpRequest httpRequest) {
		return apiService.redirectTo(USER_LIST_URL);
	}

	@ApiMapping(path = "/user/create", httpMethod = HttpMethod.POST)
	public HttpResponse saveUserFromBody(HttpRequest httpRequest) {
		return apiService.saveUserFromBody(httpRequest);
	}

	@ApiMapping(path = "/user/login", httpMethod = HttpMethod.POST)
	public HttpResponse login(HttpRequest httpRequest) {
		return apiService.login(httpRequest);
	}
}
