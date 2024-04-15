package http;

import static org.assertj.core.api.Assertions.*;

import exceptions.HttpRequestFormatException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class UriTest {
    Uri uri;

    @BeforeEach
    void setUp() {
        uri = new Uri("/test.txt", Collections.emptyMap());
    }

    @Test
    @DisplayName("URI 포맷 틀릴경우 에러")
    void uriFormatTest() {
        assertThatThrownBy(() -> new Uri("aaa", Collections.emptyMap()))
                .isInstanceOf(HttpRequestFormatException.class)
                .hasMessage("uri 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("URI MIME 생성 테스트")
    void uriGetMimeTest() {
        assertThat(uri.getMime()).isEqualTo(Mime.TXT);
    }

    @Test
    @DisplayName("URI path 획득 테스트")
    void uriGetPathTest() {
        assertThat(uri.getPath()).isEqualTo("/test.txt");
    }

    @Test
    @DisplayName("URI 정적 확장자 체크 테스트")
    void uriIsStaticResourceTest() {
        assertThat(uri.isStaticResource()).isEqualTo(true);
    }

}