package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestBody {
	private final Map<String, String> body;

	public HttpRequestBody(Map<String, String> body) {
		this.body = body;
	}

	public HttpRequestBody() {
		this.body = new HashMap<>();
	}

	public Map<String, String> getBody() {
		return Collections.unmodifiableMap(body);
	}

	public String getFromBody(String key){
		return body.get(key);
	}
}
