package hadler;

import http.HttpRequest;
import http.HttpResponse;

public interface Handler {
	HttpResponse handle(HttpRequest httpRequest);
}
