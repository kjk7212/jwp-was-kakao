package hadler;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.HttpBodyParser;
import http.HttpRequest;
import http.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final APIHandler apiHandler = new APIHandler();
    private static final ResourceHandler resourceHandler = new ResourceHandler();

    private final Socket connection;
    private Handler handler = resourceHandler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpBodyParser.HttpHeaderParsingUtils.parse(new BufferedReader(new InputStreamReader(in)));

            if (httpRequest.getUri().getExtension().isEmpty()){
                handler = apiHandler;
            }

            HttpResponse httpResponse = handler.handle(httpRequest);
            httpResponse.send(new DataOutputStream(out));
            //TODO: 예외 처리
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
