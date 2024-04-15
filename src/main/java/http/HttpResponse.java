package http;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpResponse {
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";

	private final HttpStatus httpStatus;
	private final HttpHeader httpHeader;
	private final HttpResponseBody httpResponseBody;

	public HttpResponse(HttpStatus httpStatus, HttpHeader httpHeader, HttpResponseBody httpResponseBody) {
		this.httpStatus = httpStatus;
		this.httpHeader = httpHeader;
		this.httpResponseBody = httpResponseBody;
	}

	public static HttpResponse movedPermanently(String redirectURI) {
		return new HttpResponse(HttpStatus.MOVED_PERMANENTLY, new HttpHeader(Map.of("Location", redirectURI)),
			new HttpResponseBody());
	}

	public static HttpResponse found(String redirectURI) {
		return new HttpResponse(HttpStatus.FOUND, new HttpHeader(Map.of("Location", redirectURI)),
			new HttpResponseBody());
	}

	public static HttpResponse badRequest(String errorMessage) {
		return new HttpResponse(HttpStatus.BAD_REQUEST, new HttpHeader(), new HttpResponseBody(errorMessage));
	}

	public static HttpResponse notFound(String errorMessage) {
		return new HttpResponse(HttpStatus.NOT_FOUND, new HttpHeader(), new HttpResponseBody(errorMessage));
	}

	public static HttpResponse conflict(String errorMessage) {
		return new HttpResponse(HttpStatus.CONFLICT, new HttpHeader(), new HttpResponseBody(errorMessage));
	}

	public static HttpResponse unsupportedMediaType(String errorMessage) {
		return new HttpResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, new HttpHeader(),
			new HttpResponseBody(errorMessage));
	}

	public static HttpResponse internalServerError() {
		return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, new HttpHeader(), new HttpResponseBody());
	}

	public static HttpResponse staticResource(HttpResponseBody httpResponseBody, Mime mime) {
		Map<String, String> headers = Map.of(HEADER_CONTENT_TYPE, mime.contentType, HEADER_CONTENT_LENGTH,
			Integer.toString(httpResponseBody.getLength()));
		return new HttpResponse(HttpStatus.OK, new HttpHeader(headers), httpResponseBody);
	}

	public byte[] makeByteMessage() {
		String header = httpStatus.makeStatusLine() + httpHeader.makeHeaderLine() + "\r\n";

		byte[] headerByte = header.getBytes(StandardCharsets.UTF_8);
		byte[] message = new byte[httpResponseBody.getLength() + headerByte.length];
		System.arraycopy(headerByte, 0, message, 0, headerByte.length);
		System.arraycopy(httpResponseBody.getBody(), 0, message, headerByte.length, httpResponseBody.getLength());

		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
