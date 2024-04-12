package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpHeader {
	private static final String HEADER_VALIDATION_ERROR_MESSAGE = "헤더에 잘못 된 값이 들어 있습니다";

	private static final Pattern HEADER_KEY_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+$");
	private static final Pattern HEADER_VALUE_PATTERN = Pattern.compile("^[\\x20-\\x7E]+$");

	private final Map<String, String> header;

	public HttpHeader() {
		this.header = new HashMap<>();
	}

	public HttpHeader(Map<String, String> header) {
		header.forEach((key, value) -> {
			validateHeaderValue(value);
			validateHeaderKey(key);
		});
		this.header = header;
	}

	public Map<String, String> getHeader() {
		return Collections.unmodifiableMap(header);
	}

	public void addHeader(String headerKey, String headerValue) {
		validateHeaderKey(headerKey);
		validateHeaderValue(headerValue);
		this.header.put(headerKey, headerValue);
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
}
