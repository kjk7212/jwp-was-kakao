package parser;

import http.HttpRequestBody;

public interface HttpBodyParsingStrategy {
	HttpRequestBody parse(String body);
}
