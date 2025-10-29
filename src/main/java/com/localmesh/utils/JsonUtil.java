package com.localmesh.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {
	private static final ObjectMapper mapper = new ObjectMapper();

	private JsonUtil() {}

	public static String toJson(Object obj) {
		try { return mapper.writeValueAsString(obj); }
		catch (Exception e) { throw new RuntimeException(e); }
	}

	public static <T> T fromJson(String json, Class<T> cls) {
		try { return mapper.readValue(json, cls); }
		catch (Exception e) { throw new RuntimeException(e); }
	}
}