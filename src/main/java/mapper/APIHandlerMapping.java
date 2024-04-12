package mapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class APIHandlerMapping {
	private final Map<String, Method> map;

	public APIHandlerMapping() {
		this.map = new HashMap<>();
	}

	public void addMapping(String path, Method method) {
		map.put(path, method);
	}

	public boolean contains(String path) {
		return map.containsKey(path);
	}

	public Method find(String path) {
		return map.get(path);
	}
}
