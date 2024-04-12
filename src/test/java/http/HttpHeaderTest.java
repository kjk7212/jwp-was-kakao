package http;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpHeaderTest {
	HttpHeader httpHeader;

	@BeforeEach
	void setUp() {
		httpHeader = new HttpHeader(Map.of("tset","test"));
	}

	@Test
	@DisplayName("올바르지 않은 헤더 검증 테스트")
	void newHeader() {
		assertThatThrownBy(() -> httpHeader = new HttpHeader(Map.of("^#*$^#", "$*#^%*#")))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("헤더 추가 테스트")
	void addHeader() {
		httpHeader = new HttpHeader();
		httpHeader.addHeader("test2", "test2");
		assertThat(httpHeader.getValueByKey("test2")).isEqualTo("test2");
	}

	@Test
	@DisplayName("값이 들어있는지 확인 테스트")
	void containsKey() {
		assertThat(httpHeader.containsKey("teet")).isEqualTo(false);
		assertThat(httpHeader.containsKey("tset")).isEqualTo(true);
	}

	@Test
	@DisplayName("들어 있는 값 가져오기 테스트")
	void getValueByKey() {
		assertThat(httpHeader.getValueByKey("teet")).isEqualTo(null);
		assertThat(httpHeader.getValueByKey("tset")).isEqualTo("test");

	}
}