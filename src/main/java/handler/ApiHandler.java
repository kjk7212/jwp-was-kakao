package handler;

import static constant.Constant.*;

import service.ApiService;
import annotations.ApiMapping;
import mapper.ApiMapper;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

import java.lang.reflect.Method;

public class ApiHandler implements Handler {
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
		return apiService.redirectToIndex(httpRequest);
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
