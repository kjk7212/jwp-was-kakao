package db;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.User;

class DataBaseTest {
	@BeforeEach
	void setUp() {
		DataBase.deleteAll();
		DataBase.addUser(new User("test","test","test","test"));
		DataBase.addUser(new User("test1","test2","test3","test4"));
		DataBase.addUser(new User("test3","test2","test3","test4"));
	}

	@Test
	@DisplayName("유저 찾기 테스트")
	void findUserByIdTest() {
		User user = DataBase.findUserById("test");
		assertThat(user.getUserId()).isEqualTo("test");
		assertThat(user.getEmail()).isEqualTo("test");
		assertThat(user.getPassword()).isEqualTo("test");
		assertThat(user.getPassword()).isEqualTo("test");
	}

	@Test
	@DisplayName("유저 다 찾기 테스트")
	void findAllTest() {
		assertThat(DataBase.findAll().size()).isEqualTo(3);
	}
}