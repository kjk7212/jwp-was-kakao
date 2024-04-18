package mapper;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import annotations.ApiMapping;
import handler.ApiHandler;
import http.HttpMethod;

public class ApiMapper {
	private static final Map<HttpMethod, ApiMethodMapping> ApiMappings;

	static {
		ApiMappings = Arrays.stream(HttpMethod.values())
			.collect(Collectors.toMap(Function.identity(), httpMethod -> new ApiMethodMapping()));

		Method[] methods = ApiHandler.class.getDeclaredMethods();

		Arrays.stream(methods)
			.filter(method -> method.isAnnotationPresent(ApiMapping.class))
			.map(method -> new AbstractMap.SimpleEntry<>(method.getAnnotation(ApiMapping.class), method))
			.forEach(entry -> ApiMappings.get(entry.getKey().httpMethod()).addMapping(entry.getKey().path(), entry.getValue()));
	}

	public boolean notHasMapping(HttpMethod httpMethod, String path) {
		return !ApiMappings.get(httpMethod).contains(path);
	}

	public Method getMethod(HttpMethod httpMethod, String path) {
		return ApiMappings.get(httpMethod).find(path);
	}
}
