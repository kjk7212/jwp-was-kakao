package mapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApiMethodMapping {
	// TODO 더 좋은 매핑 방식 고민해보기
	private final Map<String, Method> map;

	public ApiMethodMapping() {
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
