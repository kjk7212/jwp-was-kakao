package mapper;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import annotations.APIMapping;
import handler.APIHandler;
import http.HttpMethod;

public class APIMapper {
	private static final Map<HttpMethod, APIHandlerMapping> APIMappings;

	static {
		APIMappings = Arrays.stream(HttpMethod.values())
			.collect(Collectors.toMap(Function.identity(), httpMethod -> new APIHandlerMapping()));

		Method[] methods = APIHandler.class.getDeclaredMethods();

		Arrays.stream(methods)
			.filter(method -> method.isAnnotationPresent(APIMapping.class))
			.map(method -> new AbstractMap.SimpleEntry<>(method.getAnnotation(APIMapping.class), method))
			.forEach(entry -> APIMappings.get(entry.getKey().httpMethod()).addMapping(entry.getKey().path(), entry.getValue()));
	}

	public boolean notHasMapping(HttpMethod httpMethod, String path) {
		return !APIMappings.get(httpMethod).contains(path);
	}

	public Method getMethod(HttpMethod httpMethod, String path) {
		return APIMappings.get(httpMethod).find(path);
	}
}
