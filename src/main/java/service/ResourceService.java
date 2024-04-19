package service;

import static constant.Constant.*;

import java.io.IOException;
import java.util.Collection;

import Renderer.DynamicTemplateRenderer;
import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpResponseBody;
import http.Mime;
import model.User;
import session.SessionStorage;
import utils.FileIoUtils;

public class ResourceService {
	private static final String STATIC_RESOURCE_PATH = "./static";
	private static final String TEMPLATE_RESOURCE_PATH = "./templates";

	private static final String DYNAMIC_RESOURCE_USER_LIST = "user/list";
	private static final String DYNAMIC_RESOURCE_USER_PROFILE = "user/profile";

	private static final DynamicTemplateRenderer dynamicTemplateRenderer = new DynamicTemplateRenderer();

	public HttpResponse getStaticResource(HttpRequest httpRequest){
		try {
			Mime mime = httpRequest.getMime();
			byte[] body = FileIoUtils.loadFileFromClasspath(getResourcePath(httpRequest, mime));
			return HttpResponse.staticResource(new HttpResponseBody(body), mime);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return HttpResponse.notFound(EMPTY_ERROR_MESSAGE);
		}
	}

	private String getResourcePath(HttpRequest httpRequest, Mime mime) {
		String path = STATIC_RESOURCE_PATH;
		if (mime.isTemplate()) {
			path = TEMPLATE_RESOURCE_PATH;
		}

		return path + httpRequest.getPath();
	}

	public HttpResponse getTemplateUserList(HttpRequest httpRequest) throws IOException {
		Collection<User> users = DataBase.findAll();
		HttpResponseBody httpResponseBody = dynamicTemplateRenderer.render(DYNAMIC_RESOURCE_USER_LIST, users);

		return HttpResponse.staticResource(httpResponseBody, httpRequest.getMime());
	}

	public HttpResponse getProfile(HttpRequest httpRequest) throws IOException {
		User user = DataBase.findUserById(SessionStorage.findSession(httpRequest.getSessionId()).getUserId());
		HttpResponseBody httpResponseBody = dynamicTemplateRenderer.render(DYNAMIC_RESOURCE_USER_PROFILE, user);

		return HttpResponse.staticResource(httpResponseBody, httpRequest.getMime());
	}
}
