package http;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import session.Session;
import session.SessionStorage;

public class HttpCookie {
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
		return httpCookie.get("JSESSIONID");
	}

	public boolean hasSessionId(){
		return httpCookie.containsKey("JSESSIONID");
	}

	public void setLoginSession(String userId){
		Session session = SessionStorage.makeLoginSession(userId);
		this.httpCookie.put("Path", "/");
		this.httpCookie.put("JSESSIONID", session.getSessionId());
	}

	public String makeCookieMessage(){
		if (httpCookie.isEmpty()){
			return "";
		}

		StringBuilder cookieLine = new StringBuilder("Set-Cookie: ");
		for (String key : httpCookie.keySet().stream().sorted().collect(Collectors.toList())){
			cookieLine.append(key).append("=").append(httpCookie.get(key)).append("; ");
		}
		cookieLine.setLength(cookieLine.length() - 2);
		cookieLine.append("\n");

		return cookieLine.toString();
	}
}
