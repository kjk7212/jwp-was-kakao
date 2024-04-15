package handler;

import static constant.Constant.*;

import http.HttpResponseBody;
import utils.FileIoUtils;
import http.HttpRequest;
import http.HttpResponse;
import http.Mime;

public class ResourceHandler implements Handler {
	private static final String STATIC_RESOURCE_PATH = "./static";
	private static final String TEMPLATE_RESOURCE_PATH = "./templates";

	public HttpResponse handle(HttpRequest httpRequest) {
		try {
			Mime mime = httpRequest.getMime();
			String path = getResourcePath(httpRequest, mime);

			byte[] body = FileIoUtils.loadFileFromClasspath(path);
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
}
