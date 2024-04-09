package utils;

import static constant.Constant.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import http.HttpMethod;
import http.HttpRequest;
import http.MIME;
import http.URI;

public class HttpBodyParser {
    private final HttpBodyParsingStrategy httpBodyParsingStrategy;

    public HttpBodyParser(HttpBodyParsingStrategy httpBodyParsingStrategy) {
        this.httpBodyParsingStrategy = httpBodyParsingStrategy;
    }

    //TODO : rename
    //TODO: contentType enum으로
    public static HttpBodyParser from(String contentType) {
        if (CONTENT_TYPE_FORM.equals(contentType)) {
            return new HttpBodyParser(new FormUrlEncodedParsingStrategy());
        }
        //예외를 얘가 던지는게 맞나?... HttpRequest가 던져야하지 않나?
        throw new IllegalArgumentException("지원되지 않는 컨텐츠타입입니다.");
    }

    public Map<String, String> parse(String bodyString) {
        return httpBodyParsingStrategy.parse(bodyString);
    }

    public static class HttpHeaderParsingUtils {
        public static HttpRequest parse(BufferedReader bufferedReader) throws IOException {
            String[] f = bufferedReader.readLine().split(SPACE);
            HttpMethod httpMethod = HttpMethod.valueOf(f[0]);
            String uriString = f[1];
            String protocol = f[2];

            String line;
            Map<String, String> headers = new HashMap<>();

            while (!"".equals(line = bufferedReader.readLine()) && line != null) {
                String[] header = line.split(": ");
                headers.put(header[0], header[1]);
            }

            Map<String, String> body = Map.of();

            // (body의 존재 - Content-Type 필드의 존재) -> 필요충분조건?? 표준에선 있어야댐
            if (headers.containsKey(HEADER_CONTENT_TYPE) && headers.containsKey(HEADER_CONTENT_LENGTH)) {
                //TODO: Content-Length 정수 파싱 예외 처리...
                String contentType = String.valueOf(headers.get(HEADER_CONTENT_TYPE));
                int contentLength = Integer.parseInt(headers.get(HEADER_CONTENT_LENGTH));
                String bodyString = IOUtils.readData(bufferedReader, contentLength);
                body = from(contentType).parse(bodyString);
            }

            return new HttpRequest.Builder()
                    .uri(parseURI(uriString))
                    .body(body)
                    .protocol(protocol)
                    .headers(headers)
                    .httpMethod(httpMethod)
                    .build();
        }

        public static HttpRequest parse(String httpRequestString) {
            StringTokenizer stringTokenizer = new StringTokenizer(httpRequestString, "\n");
            String[] f = stringTokenizer.nextToken().split(SPACE);
            HttpMethod httpMethod = HttpMethod.valueOf(f[0]);
            String uriString = f[1];
            String protocol = f[2];

            String tmp;
            Map<String, String> headers = new HashMap<>();

            while (stringTokenizer.hasMoreTokens() && !(tmp = stringTokenizer.nextToken()).isEmpty()) {
                String[] header = tmp.split(HEADER_SEPARATOR);
                headers.put(header[0], header[1]);
            }

            Map<String, String> body = Map.of();

            // (body의 존재 - Content-Type 필드의 존재) -> 필요충분조건?? 표준에선 있어야댐
            if (headers.containsKey(HEADER_CONTENT_TYPE)) {
                String bodyString = body(stringTokenizer);
                body = from(String.valueOf(headers.get(HEADER_CONTENT_TYPE))).parse(bodyString);
            }

            return new HttpRequest.Builder()
                    .uri(parseURI(uriString))
                    .body(body)
                    .protocol(protocol)
                    .headers(headers)
                    .httpMethod(httpMethod)
                    .build();
        }

        private static URI parseURI(String stringURI) {
            Map<String, String> parameters = new HashMap<>();
            String path = stringURI;

            if (hasQuery(stringURI)) {
                int queryStartIndex = stringURI.indexOf(QUERY_SEPARATOR);
                path = stringURI.substring(0, queryStartIndex);
                parameters = parseParameters(stringURI);
            }
            if (hasExtension(path)) {
                stringURI = removeQuery(stringURI);
                String extension = parseExtension(stringURI);
                //TODO : 지원하지 않는 MIME인 경우에 대한 예외 처리
                return new URI(path, parameters, MIME.valueOf(extension.toUpperCase()));
            }
            return new URI(path, parameters);
        }

        public static Map<String, String> parseParameters(String stringUri) {
            Map<String, String> parameters = new HashMap<>();
            int queryStartIndex = stringUri.indexOf(QUERY_SEPARATOR) + 1;
            String query = stringUri.substring(queryStartIndex);

            List<String> queries = parseStringToList(query);

            for (String line : queries) {
                String[] parameter = line.split(PARAMETER_EQUAL_SIGN);
                parameters.put(parameter[0], parameter[1]);
            }
            return parameters;
        }

        private static String parseExtension(String stringURI) {
            int dotIndex = stringURI.lastIndexOf(EXTENSION_SEPARATOR);

            return stringURI.substring(dotIndex + 1);
        }

        private static String removeQuery(String path) {
            int index = path.indexOf("?");
            String result;
            if (index != -1) {
                result = path.substring(0, index);
            } else {
                result = path;
            }
            return result;
        }

        private static List<String> parseStringToList(String requestLine) {
            String[] requestLineArray = requestLine.split(constant.Constant.PARAMETER_SEPARATOR);

            return Arrays.stream(requestLineArray).collect(Collectors.toList());
        }

        private static boolean hasExtension(String stringURI) {
            return stringURI.contains(EXTENSION_SEPARATOR);
        }

        private static boolean hasQuery(String stringURI) {
            return stringURI.contains(QUERY_SEPARATOR);
        }


        //TODO : rename
        public static String body(StringTokenizer st) {
            StringBuilder sb = new StringBuilder();

            while (st.hasMoreTokens()) {
                sb.append(st.nextToken());

                if (st.hasMoreTokens()) {
                    sb.append(" "); // add space character
                }
            }

            return sb.toString();
        }
    }
}
