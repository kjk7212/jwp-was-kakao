package mapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import annotations.APIMapping;
import hadler.APIHandler;
import http.HttpMethod;
import webserver.URIHandlerMapping;

public class APIMapper {
	public static final Map<HttpMethod, URIHandlerMapping> APIMappings;

	static {
		APIMappings = new HashMap<>();
		for (HttpMethod httpMethod : HttpMethod.values()) {
			APIMappings.put(httpMethod, new URIHandlerMapping());
		}

		Method[] methods = APIHandler.class.getDeclaredMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(APIMapping.class)) {
				APIMapping APIMapping = method.getAnnotation(APIMapping.class);
				APIMappings.get(APIMapping.httpMethod()).addMapping(APIMapping.path(), method);
			}
		}
	}
}
