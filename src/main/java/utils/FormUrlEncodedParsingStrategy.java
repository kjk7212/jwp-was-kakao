package utils;

import static constant.Constant.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class FormUrlEncodedParsingStrategy implements HttpBodyParsingStrategy {
    @Override
    public Map<String, String> parse(String body) {
        if (body.isBlank()) {
            return Map.of();
        }

        Map<String, String> map;
        try {
            map = Arrays.stream(body.split(PARAMETER_SEPARATOR))
                    .map(s -> s.split(PARAMETER_EQUAL_SIGN))
                    .map(arr -> Map.entry(arr[0], arr[1]))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }

        return map;
    }
}
