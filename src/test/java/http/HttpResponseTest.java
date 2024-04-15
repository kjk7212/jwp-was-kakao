package http;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

	@Test
	@DisplayName("301 응답 생성테스트")
	void movedPermanently() {
		assertThat(HttpResponse.movedPermanently("/test").getHttpStatus()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
	}

	@Test
	@DisplayName("302 응답 생성테스트")
	void found() {
		assertThat(HttpResponse.found("/test").getHttpStatus()).isEqualTo(HttpStatus.FOUND);
	}

	@Test
	@DisplayName("400 응답 생성테스트")
	void badRequest() {
		assertThat(HttpResponse.badRequest("").getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("404 응답 생성테스트")
	void notFound() {
		assertThat(HttpResponse.notFound("").getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("409 응답 생성테스트")
	void conflict() {
		assertThat(HttpResponse.conflict("").getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	@DisplayName("415 응답 생성테스트")
	void unsupportedMediaTYpe() {
		assertThat(HttpResponse.unsupportedMediaType("").getHttpStatus()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Test
	@DisplayName("500 응답 생성테스트")
	void internalServerError() {
		assertThat(HttpResponse.internalServerError().getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	@DisplayName("정적리소스 응답 생성테스트")
	void staticResource() {
		assertThat(HttpResponse.staticResource(new HttpResponseBody(),MIME.CSS).getHttpStatus()).isEqualTo(HttpStatus.OK);
	}
}