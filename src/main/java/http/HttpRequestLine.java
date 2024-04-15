package http;

import java.util.Map;

public class HttpRequestLine {
	private final Uri uri;
	private final HttpMethod httpMethod;
	private final String protocol;

	public HttpRequestLine(Uri uri, HttpMethod httpMethod, String protocol) {
		this.uri = uri;
		this.httpMethod = httpMethod;
		this.protocol = protocol;
	}

	public boolean isStaticResource() {
		return uri.isStaticResource();
	}

	public Mime getMime() {
		return uri.getMime();
	}

	public String getPath() {
		return uri.getPath();
	}

	public Map<String, String> getQuery() {
		return uri.getParameters();
	}

	public String getProtocol() {
		return protocol;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
}
