package http;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MimeTest {

	@Test
	@DisplayName("템플릿 확인 테스트")
	void isTemplate() {
		Mime mime = Mime.HTML;
		Mime mime1 = Mime.JS;

		assertThat(mime.isTemplate()).isEqualTo(true);
		assertThat(mime1.isTemplate()).isEqualTo(false);
	}

	@Test
	@DisplayName("확장자로 MIME 생성테스트")
	void findMimeFromExtension() {
		assertThat(Mime.findFromExtension("txt")).isEqualTo(Mime.TXT);
		assertThat(Mime.findFromExtension("js")).isEqualTo(Mime.JS);

	}
}