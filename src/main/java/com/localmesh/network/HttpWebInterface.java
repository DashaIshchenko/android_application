package com.localmesh.network;

import com.localmesh.model.User;
import com.localmesh.storage.MessageStorage;
import com.localmesh.utils.JsonUtil;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;


public class HttpWebInterface extends NanoHTTPD {
	private final ConcurrentMap<?, User> clients;
	private final MessageStorage storage;

	public HttpWebInterface(int port, ConcurrentMap<?, User> clients, MessageStorage storage) throws IOException {
		super(port);
		this.clients = clients;
		this.storage = storage;
		start(SOCKET_READ_TIMEOUT, false);
		System.out.println("HTTP web interface started on port " + port);
	}

	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		if (uri.equalsIgnoreCase("/users")) {
			return newFixedLengthResponse(JsonUtil.toJson(clients.values()));
		} else if (uri.equalsIgnoreCase("/history")) {
			return newFixedLengthResponse(JsonUtil.toJson(storage.getHistory()));
		} else if (uri.equalsIgnoreCase("/clear")) {
			storage.clear();
			return newFixedLengthResponse("History cleared");
		} else {
			String html = "<html><body>" +
			"<h1>LocalMesh Chat</h1>" +
			"<ul>" +
			"<li><a href='/users'>Активные пользователи</a></li>" +
			"<li><a href='/history'>История сообщений</a></li>" +
			"<li><a href='/clear'>Очистить историю</a></li>" +
			"</ul>" +
			"</body></html>";
			return newFixedLengthResponse(html);
		}
	}
}