package mapper;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import annotations.DynamicResourceMapping;
import handler.ResourceHandler;

public class DynamicResourceMapper {
	private static final Map<String, Method> DynamicResourceMapping = new HashMap<>();

	static {
		Arrays.stream(ResourceHandler.class.getDeclaredMethods())
			.filter(method -> method.isAnnotationPresent(DynamicResourceMapping.class))
			.map(method -> new AbstractMap.SimpleEntry<>(method.getAnnotation(DynamicResourceMapping.class), method))
			.forEach(entry -> DynamicResourceMapping.put(entry.getKey().path(), entry.getValue()));
	}

	public boolean notHasMapping(String path) {
		return !DynamicResourceMapping.containsKey(path);
	}

	public Method getMethod(String path) {
		return DynamicResourceMapping.get(path);
	}
}
