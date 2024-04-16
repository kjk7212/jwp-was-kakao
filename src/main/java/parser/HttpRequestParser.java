package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import http.HttpCookie;
import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequestBody;
import http.HttpRequestLine;
import http.Uri;
import utils.IOUtils;

public class HttpRequestParser {
	private static final String QUERY_SEPARATOR = "?";
	private static final String SPACE = " ";
	private static final String HEADER_SEPARATOR = ": ";
	private static final String COOKIE_SEPARATOR = "; ";
	private static final String PARAMETER_EQUAL_SIGN = "=";
	private static final String HEADER_CONTENT_LENGTH = "Content-Length";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	private static final String COOKIE = "Cookie";

	private static final int HTTP_METHOD_LOCATION = 0;
	private static final int HTTP_PATH_LOCATION = 1;
	private static final int HTTP_PROTOCOL_LOCATION = 2;
	private static final int KEY_LOCATION = 0;
	private static final int VALUE_LOCATION = 1;

	private HttpBodyParsingStrategy httpBodyParsingStrategy;

	private final BufferedReader bufferedReader;

	public HttpRequestParser(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
		//TODO : 알맞은 구현체를 찾아주는 추상계층 추가하기
		this.httpBodyParsingStrategy = new FormUrlEncodedParsingStrategy();
	}

	public HttpRequestLine parseHttpRequestLine() throws IOException {
		String[] requestLine = this.bufferedReader.readLine().split(SPACE);
		HttpMethod httpMethod = HttpMethod.valueOf(requestLine[HTTP_METHOD_LOCATION]);
		String path = requestLine[HTTP_PATH_LOCATION];
		String protocol = requestLine[HTTP_PROTOCOL_LOCATION];

		return new HttpRequestLine(makeURIFromPath(path), httpMethod, protocol);
	}

	public HttpHeader parseHttpHeader() throws IOException {
		Map<String, String> httpHeader = new HashMap<>();

		String line;
		while ((line = this.bufferedReader.readLine()) != null && !line.isEmpty()) {
			String[] header = line.split(HEADER_SEPARATOR);
			httpHeader.put(header[KEY_LOCATION], header[VALUE_LOCATION]);
		}

		if(httpHeader.containsKey(COOKIE)){
			HttpCookie httpCookie = parseHttpCookie(httpHeader.get(COOKIE));
			return new HttpHeader(httpHeader, httpCookie);
		}

		return new HttpHeader(httpHeader);
	}

	public HttpCookie parseHttpCookie(String cookieString) {
		Map<String, String> cookieValue = Arrays.stream(cookieString.split(COOKIE_SEPARATOR))
			.map(cookie -> cookie.split(PARAMETER_EQUAL_SIGN))
			.collect(Collectors.toMap(cookie -> cookie[KEY_LOCATION], cookie -> cookie[VALUE_LOCATION]));
		return new HttpCookie(cookieValue);
	}

	public HttpRequestBody parseHttpRequestBody(HttpHeader httpHeader) throws IOException {
		if (!(httpHeader.containsKey(HEADER_CONTENT_TYPE) && httpHeader.containsKey(HEADER_CONTENT_LENGTH))) {
			return new HttpRequestBody();
		}
		//TODO : 알맞은 구현체를 찾아주는 추상계층 추가하기
		int contentLength = Integer.parseInt(httpHeader.getValueByKey(HEADER_CONTENT_LENGTH));
		return parseHttpBody(IOUtils.readData(bufferedReader, contentLength));
		/*
		if (CONTENT_TYPE_FORM.equals(httpHeader.getValueByKey(HEADER_CONTENT_TYPE))) {
			this.httpBodyParsingStrategy = new FormUrlEncodedParsingStrategy();
			int contentLength = Integer.parseInt(httpHeader.getValueByKey(HEADER_CONTENT_LENGTH));
			return parseHttpBody(IOUtils.readData(bufferedReader, contentLength));
		}
		throw new IllegalArgumentException("지원되지 않는 컨텐츠타입입니다.");*/
	}

	private HttpRequestBody parseHttpBody(String bodyString) {
		return httpBodyParsingStrategy.parse(bodyString);
	}

	private Uri makeURIFromPath(String path) {
		if (hasQuery(path)) {
			int queryStartIndex = path.indexOf(QUERY_SEPARATOR);
			path = path.substring(0, queryStartIndex);
			Map<String, String> parameters = parseParameters(path);

			return new Uri(path, parameters);
		}
		return new Uri(path, Collections.emptyMap());
	}

	private boolean hasQuery(String path) {
		return path.contains(QUERY_SEPARATOR);
	}

	public Map<String, String> parseParameters(String path) {
		Map<String, String> parameters = new HashMap<>();
		int queryStartIndex = path.indexOf(QUERY_SEPARATOR) + 1;
		String query = path.substring(queryStartIndex);

		List<String> queries = parseQueryToList(query);

		for (String line : queries) {
			String[] parameter = line.split(PARAMETER_EQUAL_SIGN);
			parameters.put(parameter[KEY_LOCATION], parameter[VALUE_LOCATION]);
		}
		return parameters;
	}

	private List<String> parseQueryToList(String query) {
		String[] requestLineArray = query.split(constant.Constant.PARAMETER_SEPARATOR);

		return Arrays.stream(requestLineArray).collect(Collectors.toList());
	}
}
