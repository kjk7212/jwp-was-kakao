package Renderer;

import java.io.IOException;
import java.util.Collection;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import http.HttpResponseBody;
import model.User;

public class DynamicTemplateRenderer {

	private static final String DYNAMIC_RESOURCE_PREFIX = "/templates";
	private static final String DYNAMIC_RESOURCE_SUFFIX = ".html";

	private static final TemplateLoader loader = new ClassPathTemplateLoader();
	private static final Handlebars handlebars = new Handlebars(loader);

	static {
		loader.setPrefix(DYNAMIC_RESOURCE_PREFIX);
		loader.setSuffix(DYNAMIC_RESOURCE_SUFFIX);
	}

	public HttpResponseBody render(String path, Collection<User> users) throws IOException {
		Template template = handlebars.compile(path);
		return new HttpResponseBody(template.apply(users));
	}

	public HttpResponseBody render(String path, User user) throws IOException {
		Template template = handlebars.compile(path);
		return new HttpResponseBody(template.apply(user));
	}
}
