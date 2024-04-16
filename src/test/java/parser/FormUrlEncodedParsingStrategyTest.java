package parser;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import parser.FormUrlEncodedParsingStrategy;

class FormUrlEncodedParsingStrategyTest {
    @Test
    @DisplayName("파싱 테스트")
    void parsingTest() {
        String body = "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com";

        Map<String, String> parsed = new FormUrlEncodedParsingStrategy().parse(body).getBody();

        assertThat(parsed)
                .contains(Map.entry("password", "password"))
                .contains(Map.entry("name", "이동규"))
                .contains(Map.entry("userId", "cu"))
                .contains(Map.entry("email", "brainbackdoor@gmail.com"));
    }

    @Test
    @DisplayName("뭔가 이상한 문자열 파싱 테스트")
    void wrongParsingTest() {
        String body = "userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email";
        assertThatThrownBy(() -> new FormUrlEncodedParsingStrategy().parse(body).getBody())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈값 테스트")
    void emptyMapTest() {
        String body = "";

        assertThat(new FormUrlEncodedParsingStrategy().parse(body).getBody())
                .hasSize(0);
    }

}