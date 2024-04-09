package hadler;

import utils.FileIoUtils;
import http.HttpRequest;
import http.HttpResponse;
import http.MIME;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceHandler implements Handler{
    public HttpResponse handle(HttpRequest httpRequest) {
        String path = "./static";
        MIME mime = httpRequest.getUri().getExtension().get();
        if (mime.isTemplate()) {
            path = "./templates";
        }

        byte[] body = new byte[0];
        try {
            body = FileIoUtils.loadFileFromClasspath(path + httpRequest.getUri().getPath());
        } catch (IOException | URISyntaxException e) {
            System.out.println(e);
        }

        return HttpResponse.staticResource(body, mime);
    }
}
