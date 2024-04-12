package http;

import java.nio.charset.StandardCharsets;

public class HttpResponseBody {
	private final byte[] body;

	public HttpResponseBody(String string) {
		this.body = string.getBytes(StandardCharsets.UTF_8);
	}

	public HttpResponseBody(byte[] body) {
		this.body = (body == null) ? null : body.clone();
	}

	public HttpResponseBody() {
		this.body = new byte[0];
	}

	public byte[] getBody() {
		return (body == null) ? null : body.clone();
	}

	public int getLength() {
		return body.length;
	}
}
