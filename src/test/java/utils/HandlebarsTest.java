package utils;

import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import db.DataBase;
import model.User;

public class HandlebarsTest {
    private static final Logger log = LoggerFactory.getLogger(HandlebarsTest.class);

    @Test
    @DisplayName("동적 프로파일")
    void name() throws Exception {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile("user/profile");

        User user = new User("cu", "password", "이동규", "brainbackdoor@gmail.com");
        String profilePage = template.apply(user);
        log.debug("ProfilePage : {}", profilePage);
    }


    @Test
    @DisplayName("동적 리스트")
    void list() throws Exception {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);

        Template template = handlebars.compile("user/list");

        User user = new User("cu", "password", "이동규", "brainbackdoor@gmail.com");
        DataBase.addUser(user);
        User user1 = new User("tset", "tes", "tse", "ste");
        DataBase.addUser(user1);
        Collection<User> users = DataBase.findAll();
        String profilePage = template.apply(users);
        log.debug("ProfilePage : {}", profilePage);
    }

}