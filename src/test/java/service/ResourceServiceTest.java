package service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.DataBase;
import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestBody;
import http.HttpRequestLine;
import http.Mime;
import http.Uri;
import model.User;
import session.SessionStorage;

class ResourceServiceTest {

	ResourceService resourceService = new ResourceService();

	@BeforeEach
	void setUp() {
		DataBase.deleteAll();
		User user = new User("123", "123", "123", "123");
		DataBase.addUser(user);
	}

	@Test
	@DisplayName("리스트 동적 반환 테스트")
	void getTemplateUserList() throws IOException {
		HttpRequest httpRequest = new HttpRequest(
			new HttpRequestLine(
				new Uri("/user/list.html", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody());

		assertThat(new String(resourceService.getTemplateUserList(httpRequest, Mime.HTML).makeByteMessage(), StandardCharsets.UTF_8)).contains("123");

	}

	@Test
	@DisplayName("프로파일 동적 반환 테스트")
	void getProfile() throws IOException {
		SessionStorage.makeLoginSession("123");
		HttpHeader httpHeader = new HttpHeader();
		httpHeader.makeCookieLogined("123");

		HttpRequest httpRequest = new HttpRequest(
			new HttpRequestLine(
				new Uri("/user/profile.html", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			httpHeader,
			new HttpRequestBody());

		assertThat(new String(resourceService.getProfile(httpRequest, Mime.HTML).makeByteMessage(), StandardCharsets.UTF_8)).contains("123");

	}
}