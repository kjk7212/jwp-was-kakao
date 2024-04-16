package service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.DataBase;
import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestBody;
import http.HttpRequestLine;
import http.HttpResponse;
import http.HttpStatus;
import http.Uri;
import model.User;

class ApiServiceTest {
	ApiService apiService = new ApiService();


	@Test
	@DisplayName("리다이렉트 테스트")
	void redirectToIndex() {
		HttpRequest httpRequest = new HttpRequest(
			new HttpRequestLine(
				new Uri("/", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody());

		HttpResponse httpResponse = apiService.redirectToIndex(httpRequest);

		assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
	}

	@Test
	@DisplayName("사용자 저장 테스트")
	void saveUserFromBody() {

		HttpRequest httpRequest3 = new HttpRequest(
			new HttpRequestLine(
				new Uri("/user/create", Collections.emptyMap()), HttpMethod.POST, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody(
				Map.of("password", "password","name", "이동규","userId", "cu","email", "brainbackdoor@gmail.com")));

		HttpResponse httpResponse3 = apiService.saveUserFromBody(httpRequest3);

		assertThat(httpResponse3.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
	}


	@Test
	@DisplayName("로그인 테스트")
	void login() {
		User user = new User("123", "123", "123", "123");
		DataBase.addUser(user);

		HttpRequest httpRequest3 = new HttpRequest(
			new HttpRequestLine(
				new Uri("/user/login", Collections.emptyMap()), HttpMethod.POST, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody(Map.of("password", "123","userId", "123")));

		HttpResponse httpResponse3 = apiService.login(httpRequest3);

		assertThat(httpResponse3.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
		assertThat(new String(httpResponse3.makeByteMessage(), StandardCharsets.UTF_8)).contains("Set-Cookie");

	}
}