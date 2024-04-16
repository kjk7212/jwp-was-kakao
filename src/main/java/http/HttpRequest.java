package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import parser.HttpRequestParser;

public class HttpRequest {
	private final HttpRequestLine httpRequestLine;
	private final HttpHeader httpHeader;
	private final HttpRequestBody httpRequestBody;

	public HttpRequest(HttpRequestLine httpRequestLine, HttpHeader httpHeader, HttpRequestBody httpRequestBody) {
		this.httpRequestLine = httpRequestLine;
		this.httpHeader = httpHeader;
		this.httpRequestBody = httpRequestBody;
	}

	public static HttpRequest createHttpRequestFromInputStream(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		HttpRequestParser httpRequestParser = new HttpRequestParser(bufferedReader);

		HttpRequestLine httpRequestLine = httpRequestParser.parseHttpRequestLine();
		HttpHeader httpHeader = httpRequestParser.parseHttpHeader();

		HttpRequestBody httpRequestBody = httpRequestParser.parseHttpRequestBody(httpHeader);

		return new HttpRequest(httpRequestLine, httpHeader, httpRequestBody);
	}

	private HttpRequest(Builder builder) {
		this(builder.httpRequestLine,
			builder.httpHeader,
			builder.httpRequestBody);
	}

	public static class Builder {
		private HttpRequestLine httpRequestLine;
		private HttpHeader httpHeader;
		private HttpRequestBody httpRequestBody;

		public Builder httpRequestLine(HttpRequestLine httpRequestLine) {
			this.httpRequestLine = httpRequestLine;
			return this;
		}

		public Builder httpHeaders(HttpHeader httpHeader) {
			this.httpHeader = httpHeader;
			return this;
		}

		public Builder httpBody(HttpRequestBody httpRequestBody) {
			this.httpRequestBody = httpRequestBody;
			return this;
		}

		public HttpRequest build() {
			return new HttpRequest(this);
		}
	}

	public HttpMethod getHttpMethod() {
		return httpRequestLine.getHttpMethod();
	}

	public String getPath() {
		return httpRequestLine.getPath();
	}

	public String getProtocol() {
		return httpRequestLine.getProtocol();
	}

	public Map<String, String> getHttpHeader() {
		return httpHeader.getHeader();
	}

	public Map<String, String> getHttpBody() {
		return httpRequestBody.getBody();
	}

	public boolean hasCookie(){
		return httpHeader.hasCookie();
	}

	public String getSessionId(){
		return httpHeader.getSessionId();
	}

	public String getFromBody(String key){
		return this.httpRequestBody.getFromBody(key);
	}

	public boolean isStaticResource() {
		return httpRequestLine.isStaticResource();
	}

	public Mime getMime() {
		return httpRequestLine.getMime();
	}
}
