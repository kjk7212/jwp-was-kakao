package handler;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestBody;
import http.HttpRequestLine;
import http.HttpResponse;
import http.HttpStatus;
import http.Uri;

class APIHandlerTest {

	@Test
	@DisplayName("각종 메소드 매핑 테스트")
	void handle() {
		HttpRequest httpRequest = new HttpRequest(
			new HttpRequestLine(
				new Uri("/", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody());

		HttpRequest httpRequest2 = new HttpRequest(
			new HttpRequestLine(
				new Uri("/test", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody());

		HttpRequest httpRequest3 = new HttpRequest(
			new HttpRequestLine(
				new Uri("/user/create", Collections.emptyMap()), HttpMethod.POST, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody(Map.of()));

		APIHandler apiHandler = new APIHandler();
		HttpResponse httpResponse = apiHandler.handle(httpRequest);
		HttpResponse httpResponse2 = apiHandler.handle(httpRequest2);
		HttpResponse httpResponse3 = apiHandler.handle(httpRequest3);

		assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
		assertThat(httpResponse2.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(httpResponse3.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("리다이렉트 테스트")
	void redirectToIndex() {
		HttpRequest httpRequest = new HttpRequest(
			new HttpRequestLine(
				new Uri("/", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody());

		APIHandler apiHandler = new APIHandler();
		HttpResponse httpResponse = apiHandler.handle(httpRequest);

		assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
	}

	@Test
	@DisplayName("사용자 저장 테스트")
	void saveUserFromBody() {

		HttpRequest httpRequest3 = new HttpRequest(
			new HttpRequestLine(
				new Uri("/user/create", Collections.emptyMap()), HttpMethod.POST, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody(Map.of("password", "password","name", "이동규","userId", "cu","email", "brainbackdoor@gmail.com")));

		APIHandler apiHandler = new APIHandler();
		HttpResponse httpResponse3 = apiHandler.handle(httpRequest3);

		assertThat(httpResponse3.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
	}
}