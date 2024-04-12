package handler;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestBody;
import http.HttpRequestLine;
import http.HttpResponse;
import http.HttpStatus;
import http.URI;

class ResourceHandlerTest {

	@Test
	@DisplayName("있는 자원 요청 테스트")
	void handleResourceHave() {
		ResourceHandler resourceHandler = new ResourceHandler();
		HttpResponse httpResponse = resourceHandler.handle(new HttpRequest(
			new HttpRequestLine(
				new URI("/index.html", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody()));

		assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("없는 자원 요청 테스트")
	void handleResourceNotHave() {
		ResourceHandler resourceHandler = new ResourceHandler();
		HttpResponse httpResponse = resourceHandler.handle(new HttpRequest(
			new HttpRequestLine(
				new URI("/index.htm", Collections.emptyMap()), HttpMethod.GET, "HTTP/1.1"),
			new HttpHeader(),
			new HttpRequestBody()));

		assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}