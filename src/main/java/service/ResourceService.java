package service;

import static constant.Constant.*;

import java.io.IOException;
import java.util.Collection;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

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

	private static final String DYNAMIC_RESOURCE_PREFIX = "/templates";
	private static final String DYNAMIC_RESOURCE_SUFFIX = ".html";
	private static final String DYNAMIC_RESOURCE_USER_LIST = "user/list";
	private static final String DYNAMIC_RESOURCE_USER_PROFILE = "user/profile";

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

	//----------------------------------------------------------------------------------

	public HttpResponse getTemplateUserList(HttpRequest httpRequest) throws IOException {
		Collection<User> users = DataBase.findAll();

		TemplateLoader loader = new ClassPathTemplateLoader();
		loader.setPrefix(DYNAMIC_RESOURCE_PREFIX);
		loader.setSuffix(DYNAMIC_RESOURCE_SUFFIX);
		Handlebars handlebars = new Handlebars(loader);

		Template template = handlebars.compile(DYNAMIC_RESOURCE_USER_LIST);
		HttpResponseBody httpResponseBody = new HttpResponseBody(template.apply(users));

		return HttpResponse.staticResource(httpResponseBody, httpRequest.getMime());
	}


	public HttpResponse getProfile(HttpRequest httpRequest) throws IOException {
		User user = DataBase.findUserById(SessionStorage.findSession(httpRequest.getSessionId()).getUserId());

		TemplateLoader loader = new ClassPathTemplateLoader();
		loader.setPrefix(DYNAMIC_RESOURCE_PREFIX);
		loader.setSuffix(DYNAMIC_RESOURCE_SUFFIX);
		Handlebars handlebars = new Handlebars(loader);

		Template template = handlebars.compile(DYNAMIC_RESOURCE_USER_PROFILE);
		HttpResponseBody httpResponseBody = new HttpResponseBody(template.apply(user));

		return HttpResponse.staticResource(httpResponseBody, httpRequest.getMime());
	}
}
