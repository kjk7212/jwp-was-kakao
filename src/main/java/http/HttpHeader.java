package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpHeader {
	private static final String HEADER_CONTENT_TYPE = "Content-Type";

	private static final String HEADER_VALIDATION_ERROR_MESSAGE = "헤더에 잘못 된 값이 들어 있습니다";

	private static final Pattern HEADER_KEY_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+$");
	private static final Pattern HEADER_VALUE_PATTERN = Pattern.compile("^[\\x20-\\x7E]+$");

	private final Map<String, String> header;

	private final HttpCookie httpCookie;

	public HttpHeader() {
		this.header = new HashMap<>();
		this.httpCookie = new HttpCookie();
	}

	public HttpHeader(Map<String, String> header) {
		header.forEach((key, value) -> {
			validateHeaderValue(value);
			validateHeaderKey(key);
		});
		this.header = header;
		this.httpCookie = new HttpCookie();
	}

	public HttpHeader(Map<String, String> header, HttpCookie httpCookie) {
		header.forEach((key, value) -> {
			validateHeaderValue(value);
			validateHeaderKey(key);
		});
		this.header = header;
		this.httpCookie = httpCookie;
	}

	public Map<String, String> getHeader() {
		return Collections.unmodifiableMap(header);
	}

	public void addHeader(String headerKey, String headerValue) {
		validateHeaderKey(headerKey);
		validateHeaderValue(headerValue);
		this.header.put(headerKey, headerValue);
	}

	public void makeCookieLogined(String userId){
		this.httpCookie.setLoginSession(userId);
	}

	private void validateHeaderKey(String headerKey) {
		if (headerKey == null || !HEADER_KEY_PATTERN.matcher(headerKey).matches()) {
			throw new IllegalArgumentException(HEADER_VALIDATION_ERROR_MESSAGE);
		}
	}

	private void validateHeaderValue(String headerValue) {
		if (headerValue == null || !HEADER_VALUE_PATTERN.matcher(headerValue).matches() && !headerValue.contains("\n")
			&& !headerValue.contains("\r")) {
			throw new IllegalArgumentException(HEADER_VALIDATION_ERROR_MESSAGE);
		}
	}

	public boolean containsKey(String string) {
		return header.containsKey(string);
	}

	public String getValueByKey(String key) {
		return header.get(key);
	}

	public String makeHeaderLine(){
		StringBuilder headerLine = new StringBuilder(httpCookie.makeCookieMessage());
		for (String key : header.keySet()){
			headerLine.append(makeHeaderToString(key, header.get(key)));
		}

		return headerLine.toString();
	}

	private String makeHeaderToString(String key, String value) {
		String suffix = HEADER_CONTENT_TYPE.equals(key) ? ";charset=utf-8" : "";
		return key + ": " + value + suffix + "\r\n";
	}

	public boolean hasCookie(){
		return this.httpCookie.hasCookie();
	}

	public String getSessionId(){
		return this.httpCookie.getSessionId();
	}

}
