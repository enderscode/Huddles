package com.luisibanez.huddles.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public final class JsonUtils {
	
	public static final int BUFFER_SIZE = 0x1000;
	
	public static boolean optBoolean(JSONObject json, String key)
			throws JSONException {
		return optBoolean(json, key, false);
	}

	public static boolean optBoolean(JSONObject json, String key,
			boolean defaultVal) throws JSONException {
		try {
			return json.has(key) ? json.getBoolean(key) : defaultVal;
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static String optString(JSONObject json, String name) {
		return optString(json, name, "");
	}

	public static String optString(JSONObject json, String name, String fallback) {
		if (!json.isNull(name)) {
			return json.optString(name);
		}
		return fallback;
	}

	public static JSONObject getJsonFrom(Response response) throws JSONException{
		BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        String result = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			result = getBodyString(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JSONObject(result);
	}
				
	private static String getBodyString(Response response) throws IOException {
		TypedInput body = response.getBody();
		if (body != null) {

			if (!(body instanceof TypedByteArray)) {
				response = readBodyToBytesIfNecessary(response);
				body = response.getBody();
			}

			byte[] bodyBytes = ((TypedByteArray) body).getBytes();
			String bodyMime = body.mimeType();
			String bodyCharset = MimeUtil.parseCharset(bodyMime);
			return new String(bodyBytes, bodyCharset);
		}

		return null;
	}

	private static Response readBodyToBytesIfNecessary(Response response)
			throws IOException {
		TypedInput body = response.getBody();
		if (body == null || body instanceof TypedByteArray) {
			return response;
		}

		String bodyMime = body.mimeType();
		byte[] bodyBytes = streamToBytes(body.in());
		body = new TypedByteArray(bodyMime, bodyBytes);

		return replaceResponseBody(response, body);
	}

	private static Response replaceResponseBody(Response response, TypedInput body) {
		return new Response(response.getUrl(), response.getStatus(),
				response.getReason(), response.getHeaders(), body);
	}

	private static byte[] streamToBytes(InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (stream != null) {
			byte[] buf = new byte[BUFFER_SIZE];
			int r;
			while ((r = stream.read(buf)) != -1) {
				baos.write(buf, 0, r);
			}
		}
		return baos.toByteArray();
	}
}
