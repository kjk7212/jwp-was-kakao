package session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionStorage {
	private static final Map<String, Session> sessionStorage = new HashMap<>();

	public static Session makeLoginSession(String userId){
		String uuid = UUID.randomUUID().toString();
		Session session = new Session(uuid);
		session.addProperties("logined", "true");
		session.addProperties("userId", userId);

		sessionStorage.put(uuid, session);
		return session;
	}

	public static boolean isLogined(String sessionId){
		Session session = findSession(sessionId);
		if (session == null){
			return false;
		}
		return session.isLogined();
	}

	private SessionStorage(){
	}

	public boolean hasSession(String sessionId) {
		return sessionStorage.containsKey(sessionId);
	}

	public static Session findSession(String sessionId){
		return sessionStorage.get(sessionId);
	}

}
