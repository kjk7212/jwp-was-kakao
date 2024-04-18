package parser;

import http.HttpHeader;
import http.HttpRequestBody;

public class ParsingStrategyFactory {
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

	public static HttpBodyParsingStrategy getParsingStrategy(HttpHeader httpHeader){
		if (!(httpHeader.containsKey(HEADER_CONTENT_TYPE) && httpHeader.containsKey(HEADER_CONTENT_LENGTH))) {
			return body -> new HttpRequestBody();
		}

		if (CONTENT_TYPE_FORM.equals(httpHeader.getValueByKey(HEADER_CONTENT_TYPE))) {
			return new FormUrlEncodedParsingStrategy();
		}
		throw new IllegalArgumentException("지원되지 않는 컨텐츠타입입니다.");
	}
}
