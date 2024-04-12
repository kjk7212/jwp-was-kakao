package http;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MIMETest {

	@Test
	@DisplayName("템플릿 확인 테스트")
	void isTemplate() {
		MIME mime = MIME.HTML;
		MIME mime1 = MIME.JS;

		assertThat(mime.isTemplate()).isEqualTo(true);
		assertThat(mime1.isTemplate()).isEqualTo(false);
	}

	@Test
	@DisplayName("확장자로 MIME 생성테스트")
	void findMimeFromExtension() {
		assertThat(MIME.findMimeFromExtension("txt")).isEqualTo(MIME.TXT);
		assertThat(MIME.findMimeFromExtension("js")).isEqualTo(MIME.JS);

	}
}