package handler;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.HttpRequest;
import http.HttpResponse;
import mapper.NeedAuthorizedMapper;
import session.SessionStorage;

public class RequestHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private static final ApiHandler apiHandler = new ApiHandler();
	private static final ResourceHandler resourceHandler = new ResourceHandler();
	private static final NeedAuthorizedMapper needAuthorizedMapper = new NeedAuthorizedMapper();

	private static final String LOGIN_PAGE_LOCATION = "/user/login.html";

	private final Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			HttpRequest httpRequest = HttpRequest.createHttpRequestFromInputStream(in);

			HttpResponse httpResponse = makeResponse(httpRequest);
			sendResponse(httpResponse.makeByteMessage(), new DataOutputStream(out));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private HttpResponse makeResponse(HttpRequest httpRequest){
		if (needAuthorizedMapper.needAuthorize(httpRequest.getPath())
			&& !(httpRequest.hasCookie() && SessionStorage.isLogined(httpRequest.getSessionId()))){
			return HttpResponse.found(LOGIN_PAGE_LOCATION);
		}

		Handler handler = apiHandler;
		if (httpRequest.isStaticResource()) {
			handler = resourceHandler;
		}

		return handler.handle(httpRequest);
	}

	private void sendResponse(byte[] message, DataOutputStream dataOutputStream) {
		try {
			dataOutputStream.write(message, 0, message.length);
			dataOutputStream.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
