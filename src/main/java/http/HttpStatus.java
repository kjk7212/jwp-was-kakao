package http;

public enum HttpStatus {
	OK(200, "OK"),
	CREATED(201, "Created"),
	NO_CONTENT(204, "No Content"),
	MOVED_PERMANENTLY(301, "Moved Permanently"),
	FOUND(302, "Found"),
	SEE_OTHER(303, "See Other"),
	NOT_MODIFIED(304, "Not Modified"),
	TEMPORARY_REDIRECT(307, "Temporary Redirect"),
	PERMANENT_REDIRECT(308, "Permanent Redirect"),
	BAD_REQUEST(400, "Bad Request"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Not Found"),
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	CONFLICT(409, "Conflict"),
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	SERVICE_UNAVAILABLE(503, "Service Unavailable");

	private final int code;
	private final String description;

	HttpStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
