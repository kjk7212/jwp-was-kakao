package parser;

import static constant.Constant.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import http.HttpRequestBody;

public class FormUrlEncodedParsingStrategy implements HttpBodyParsingStrategy {
	private static final String PARAMETER_EQUAL_SIGN = "=";

	//TODO : 주소에 []로 직접접근하기 때문에 쌍이 모자랄때 => key & value 페어가 맞춰지지 않을때는 확실히 에러를 뱉는데, 그 외에도 다른 검증 로직 추가를 검토해보자.
	@Override
	public HttpRequestBody parse(String body) {
		if (body.isBlank()) {
			return new HttpRequestBody();
		}

		try {
			Map<String, String> parsedBody = Arrays.stream(body.split(PARAMETER_SEPARATOR))
				.map(s -> s.split(PARAMETER_EQUAL_SIGN))
				.collect(Collectors.toMap(arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8), arr -> URLDecoder.decode(arr[1], StandardCharsets.UTF_8)));

			return new HttpRequestBody(parsedBody);
		} catch (Exception e) {
			throw new IllegalArgumentException("유효하지 않은 형식입니다.");
		}
	}
}
