package http;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Mime {
	HTML("text/html", "html"),
	CSS("text/css", "css"),
	JS("application/javascript", "js"),
	ICO("image/x-icon", "ico"),
	PNG("image/png", "png"),
	JPG("image/jpeg", "jpg"),
	TXT("text/plain", "txt"),
	EOT("font/EOT", "eot"),
	SVG("font/SVG", "svg"),
	WOFF("font/WOFF", "woff"),
	WOFF2("font/WOFF2", "woff2"),
	TTF("font/ttf", "ttf");

	public final String contentType;
	public final String extension;

	Mime(String contentType, String extension) {
		this.contentType = contentType;
		this.extension = extension;
	}

	private String getExtension() {
		return extension;
	}

	public boolean isTemplate() {
		return this.equals(Mime.HTML) || this.equals(Mime.TXT);
	}

	public static Mime findFromExtension(String extension) {
		return Arrays.stream(values())
			.filter(mime -> mime.getExtension().equals(extension))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("해당 확장자는 지원하지 않습니다."));
	}
}