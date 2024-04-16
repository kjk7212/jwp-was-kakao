package handler;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.HttpRequest;
import http.HttpResponse;

public class RequestHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private static final ApiHandler apiHandler = new ApiHandler();
	private static final ResourceHandler resourceHandler = new ResourceHandler();

	private final Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			HttpRequest httpRequest = HttpRequest.createHttpRequestFromInputStream(in);

			Handler handler = apiHandler;
			if (httpRequest.isStaticResource()) {
				handler = resourceHandler;
			}

			HttpResponse httpResponse = handler.handle(httpRequest);
			sendResponse(httpResponse.makeByteMessage(), new DataOutputStream(out));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
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
