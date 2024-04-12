package parser;

import static constant.Constant.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import http.HttpRequestBody;

public class FormUrlEncodedParsingStrategy implements HttpBodyParsingStrategy {
	private static final String PARAMETER_EQUAL_SIGN = "=";

	@Override
	public HttpRequestBody parse(String body) {
		if (body.isBlank()) {
			return new HttpRequestBody();
		}

		try {
			return new HttpRequestBody(Arrays.stream(body.split(PARAMETER_SEPARATOR))
				.map(s -> s.split(PARAMETER_EQUAL_SIGN))
				.collect(Collectors.toMap(arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8), arr -> URLDecoder.decode(arr[1], StandardCharsets.UTF_8))));
		} catch (Exception e) {
			throw new IllegalArgumentException("유효하지 않은 형식입니다.");
		}
	}
}
