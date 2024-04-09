package hadler;

import static mapper.APIMapper.*;

import annotations.APIMapping;
import db.DataBase;
import model.User;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

import java.lang.reflect.Method;
import java.util.Map;

public class APIHandler implements Handler {

    public HttpResponse handle(HttpRequest httpRequest) {
        HttpMethod httpMethod = httpRequest.getHttpMethod();
        String path = httpRequest.getUri().getPath();
        if (!APIMappings.get(httpMethod).contains(path)) {
            //TODO : 404
            throw new RuntimeException("404");
        }
        Method method = APIMappings.get(httpMethod).find(path);

        try{
            return (HttpResponse) method.invoke(this, httpRequest);
        } catch (Exception e) {
            System.out.println(e);
        }
        return HttpResponse.internalServerError();
    }

    @APIMapping(path = "/user/create", httpMethod = HttpMethod.POST)
    public HttpResponse saveUser(HttpRequest httpRequest) {
        Map<String, String> parameters = httpRequest.getBody();
        //TODO : NPE 예외처리
        String name = parameters.get("name");
        String email = parameters.get("email");
        String password = parameters.get("password");
        String userId = parameters.get("userId");

        if (DataBase.findUserById(userId) != null) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }
        DataBase.addUser(new User(userId, password, name, email));

        return HttpResponse.found("http://localhost:8080/index.html");
    }
}
