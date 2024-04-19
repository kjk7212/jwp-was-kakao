package session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionStorage {
	public static final String LOGINED = "logined";
	public static final String USER_ID = "userId";
	public static final String TRUE = "true";

	private static final Map<String, Session> sessionStorage = new HashMap<>();

	public static Session makeLoginSession(String userId){
		String uuid = UUID.randomUUID().toString();
		Session session = new Session(uuid);
		session.addProperties(LOGINED, TRUE);
		session.addProperties(USER_ID, userId);

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
