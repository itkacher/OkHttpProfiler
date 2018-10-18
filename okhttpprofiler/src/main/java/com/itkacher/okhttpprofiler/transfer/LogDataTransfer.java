package com.itkacher.okhttpprofiler.transfer;

import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogDataTransfer implements DataTransfer {
    private static final int LOG_LENGTH = 255;
    private static final String LOG_PREFIX = "OKPRFL";
    private static final String DELIMITER = "_";
    private static final String HEADER_DELIMITER = ":";

    @Override
    public void sendRequest(String id, Request request) {
        log(id, MessageType.REQUEST_METHOD, request.method());
        String url = request.url().toString();
        log(id, MessageType.REQUEST_URL, url);
        log(id, MessageType.REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
        Headers headers = request.headers();
        if (headers != null) {
            for (String name : headers.names()) {
                log(id, MessageType.REQUEST_HEADER, name + ":" + headers.get(name));
            }
        }

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            RequestBody body = copy.body();
            if (body != null) {
                body.writeTo(buffer);
                largeLog(id, MessageType.REQUEST_BODY, buffer.readUtf8());
            }
        } catch (final IOException ignored) {
        }
    }

    @Override
    public void sendResponse(String id, Response response) {
        Headers headers = response.headers();
        log(id, MessageType.RESPONSE_STATUS, String.valueOf(response.code()));
        if (headers != null) {
            for (String name : headers.names()) {
                log(id, MessageType.RESPONSE_HEADER, name + HEADER_DELIMITER + headers.get(name));
            }
        }

        try {
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            largeLog(id, MessageType.RESPONSE_BODY, responseBodyCopy.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendException(String id, Exception response) {
        log(id, MessageType.RESPONSE_ERROR, response.getLocalizedMessage());
    }

    @Override
    public void sendDuration(String id, long duration) {
        log(id, MessageType.RESPONSE_TIME, String.valueOf(duration));
        log(id, MessageType.RESPONSE_END, "-->");
    }

    private void log(String id, MessageType type, String message) {
        Log.d(LOG_PREFIX + DELIMITER + id + DELIMITER + type.name, message);
    }

    private void largeLog(String id, MessageType type, String content) {
        if (content.length() > LOG_LENGTH) {
            String part = content.substring(0, LOG_LENGTH);
            log(id, type, part);
            largeLog(id, type, content.substring(LOG_LENGTH));
        } else {
            log(id, type, content);
        }
    }
}
