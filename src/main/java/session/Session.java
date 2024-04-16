package session;

import java.util.HashMap;
import java.util.Map;

public class Session {
	private final String sessionId;
	private final Map<String, String> properties;

	public Session(String sessionId){
		this.sessionId = sessionId;
		properties = new HashMap<>();
	}

	public boolean isLogined(){
		if(properties.containsKey("logined")) {
			return Boolean.parseBoolean(properties.get("logined"));
		}
		return false;
	}

	public void addProperties(String key, String value){
		properties.put(key, value);
	}

	public String getSessionId(){
		return sessionId;
	}

	public String getUserId(){
		return properties.get("userId");
	}
}
