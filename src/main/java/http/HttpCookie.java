package http;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import session.Session;
import session.SessionStorage;

public class HttpCookie {
	private static final String COOKIE_DATA_PATH_KEY = "Path";
	public static final String COOKIE_DATA_JSESSIONID_KEY = "JSESSIONID";
	public static final String COOKIE_DATA_PATH_VALUE = "/";

	public static final String COOKIE_MESSAGE_HEADER_STRING_SET_COOKIE = "Set-Cookie: ";
	public static final String COOKIE_DATA_KEY_VALUE_SEPARATOR = "=";
	public static final String COOKIE_MESSAGE_DATA_SEPARATOR = "; ";

	private final Map<String, String> httpCookie;

	public HttpCookie(){
		httpCookie = new HashMap<>();
	}

	public HttpCookie(Map<String, String> httpCookie) {
		this.httpCookie = httpCookie;
	}

	public boolean hasCookie(){
		return !httpCookie.isEmpty();
	}

	public String getSessionId(){
		return httpCookie.get(COOKIE_DATA_JSESSIONID_KEY);
	}

	public boolean hasSessionId(){
		return httpCookie.containsKey(COOKIE_DATA_JSESSIONID_KEY);
	}

	public void setLoginSession(String userId){
		Session session = SessionStorage.makeLoginSession(userId);
		this.httpCookie.put(COOKIE_DATA_PATH_KEY, COOKIE_DATA_PATH_VALUE);
		this.httpCookie.put(COOKIE_DATA_JSESSIONID_KEY, session.getSessionId());
	}

	public String makeCookieMessage(){
		if (httpCookie.isEmpty()){
			return "";
		}

		StringBuilder cookieLine = new StringBuilder(COOKIE_MESSAGE_HEADER_STRING_SET_COOKIE);
		for (String key : httpCookie.keySet().stream().sorted().collect(Collectors.toList())){
			cookieLine.append(key).append(COOKIE_DATA_KEY_VALUE_SEPARATOR).append(httpCookie.get(key)).append(
				COOKIE_MESSAGE_DATA_SEPARATOR);
		}
		cookieLine.setLength(cookieLine.length() - 2);
		cookieLine.append("\n");

		return cookieLine.toString();
	}
}
