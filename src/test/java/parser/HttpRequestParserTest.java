package parser;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import http.HttpMethod;
import http.HttpRequest;

public class HttpRequestParserTest {
    @Test
    @DisplayName("HTTP 문자열 HttpRequest 객체로 파싱 하는 테스트")
    void parsingAllInOneTest() throws IOException {
        String httpRequestString = "GET /index.html HTTP/1.1\n" +
                "Cookie: JSESSIONID=12312341324; Path=/\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*";

        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes(StandardCharsets.UTF_8));
        HttpRequest httpRequest = HttpRequest.createHttpRequestFromInputStream(inputStream);

        assertThat(httpRequest.getHttpMethod())
                .isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getSessionId())
            .isEqualTo("12312341324");
        assertThat(httpRequest.getHttpBody())
                .isEqualTo(Map.of());
        assertThat(httpRequest.getPath())
                .isEqualTo("/index.html");
        assertThat(httpRequest.getHttpHeader())
                .contains(Map.entry("Connection", "keep-alive"))
                .contains(Map.entry("Accept", "*/*"))
                .contains(Map.entry("Host", "localhost:8080"));
        assertThat(httpRequest.getProtocol())
                .isEqualTo("HTTP/1.1");
    }
}
