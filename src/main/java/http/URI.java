package http;

import exceptions.HttpRequestFormatException;

import java.util.Map;

public class URI {
	private static final String EXTENSION_SEPARATOR = ".";

	private final String path;
	private final Map<String, String> parameters;

	public URI(String path, Map<String, String> parameters) {
		validatePathFormat(path);
		this.path = path;
		this.parameters = parameters;
	}

	private void validatePathFormat(String path) {
		if (!path.startsWith("/")) {
			throw new HttpRequestFormatException("uri 형식이 올바르지 않습니다.");
		}
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public boolean isStaticResource() {
		return path.contains(EXTENSION_SEPARATOR);
	}

	public MIME getMime() {
		return MIME.findMimeFromExtension(getExtensionFromPath());
	}

	private String getExtensionFromPath() {
		return path.substring(path.lastIndexOf(EXTENSION_SEPARATOR) + 1);
	}
}
