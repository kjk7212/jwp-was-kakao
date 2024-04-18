package handler;

import java.io.IOException;
import java.lang.reflect.Method;

import service.ResourceService;
import annotations.DynamicResourceMapping;
import annotations.NeedAuthorized;
import http.HttpRequest;
import http.HttpResponse;
import http.Mime;
import mapper.DynamicResourceMapper;

public class ResourceHandler implements Handler {
	private static final DynamicResourceMapper dynamicResourceMapper = new DynamicResourceMapper();
	private static final ResourceService resourceService = new ResourceService();

	public HttpResponse handle(HttpRequest httpRequest) {
		String path = httpRequest.getPath();

		if (dynamicResourceMapper.notHasMapping(path)) {
			return resourceService.getStaticResource(httpRequest);
		}

		Method method = dynamicResourceMapper.getMethod(path);
		try {
			return (HttpResponse)method.invoke(this, httpRequest);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return HttpResponse.internalServerError();
		}
	}

	@NeedAuthorized
	@DynamicResourceMapping(path = "/user/list.html")
	public HttpResponse getTemplateUserList(HttpRequest httpRequest) throws IOException {
		return resourceService.getTemplateUserList(httpRequest);
	}


	@NeedAuthorized
	@DynamicResourceMapping(path = "/user/profile.html")
	public HttpResponse getProfile(HttpRequest httpRequest) throws IOException {
		return resourceService.getProfile(httpRequest);
	}
}
