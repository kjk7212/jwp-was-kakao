package session;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SessionStorageTest {

	@Test
	void makeLoginSession() {
		Session session = SessionStorage.makeLoginSession("test");

		assertThat(SessionStorage.findSession(session.getSessionId()))
			.isEqualTo(session);
		assertThat(session.getUserId())
			.isEqualTo("test");
	}

	@Test
	void isLogined() {
		Session session = SessionStorage.makeLoginSession("test");
		assertThat(SessionStorage.isLogined(session.getSessionId()))
			.isEqualTo(true);
	}
}