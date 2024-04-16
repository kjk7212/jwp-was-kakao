package mapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import annotations.ApiMapping;
import annotations.DynamicResourceMapping;
import annotations.NeedAuthorized;
import handler.ApiHandler;
import handler.Handler;
import handler.ResourceHandler;

public class NeedAuthorizedMapper {
	private static final List<Class<? extends Handler>> CLASS_TO_SCAN = List.of(ResourceHandler.class, ApiHandler.class);
	private static final Set<String> NeedAuthorizedPath = new HashSet<>();

	static {
		List<Method> methods = CLASS_TO_SCAN.stream().map(Class::getDeclaredMethods).flatMap(Arrays::stream)
			.filter(method -> method.isAnnotationPresent(NeedAuthorized.class))
			.collect(Collectors.toList());

		methods.stream()
			.filter(method -> (method.isAnnotationPresent(DynamicResourceMapping.class) || method.isAnnotationPresent(ApiMapping.class)))
			.map(method -> method.isAnnotationPresent(DynamicResourceMapping.class) ? method.getAnnotation(DynamicResourceMapping.class).path() : method.getAnnotation(ApiMapping.class).path())
			.forEach(NeedAuthorizedPath::add);
	}

	public boolean needAuthorize(String path) {
		return NeedAuthorizedPath.contains(path);
	}
}
