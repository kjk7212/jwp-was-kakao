package http;

import static constant.Constant.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpResponse {
    private final StatusCode statusCode;
    private final Map<String, String> headers;
    private final byte[] body;

    public static HttpResponse found(String redirectURI) {
        //302일 때 응답 헤더에 필요한 값은??
        return new HttpResponse(StatusCode.FOUND, new byte[0], Map.of("Location", redirectURI));
    }

    public static HttpResponse internalServerError() {
        return new HttpResponse(StatusCode.INTERNAL_SERVER_ERROR, new byte[0], Map.of());
    }

    public static HttpResponse staticResource(byte[] body, MIME mime) {
        Map<String, String> headers = Map.of(HEADER_CONTENT_TYPE, mime.contentType, HEADER_CONTENT_LENGTH, Integer.toString(body.length));
        return new HttpResponse(StatusCode.OK, body, headers);
    }

    public HttpResponse(StatusCode statusCode, byte[] body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public void send(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getDescription() + " \r\n");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                writeHeader(dos, entry.getKey(), entry.getValue());
            }
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            //TODO: 예외처리
            System.out.println(e.getMessage());
        }
    }

    private void writeHeader(DataOutputStream dos, String key, String value) throws IOException {
        String string = key + ": " + value;
        if (HEADER_CONTENT_TYPE.equals(key)) {
            string += ";charset=utf-8";
        }
        dos.writeBytes(string + "\r\n");
    }
}
