package com.itkacher.okhttpprofiler.transfer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogDataTransfer implements DataTransfer {
    private static final int LOG_LENGTH = 4000;
    private static final int SLOW_DOWN_PARTS_AFTER = 20;
    private static final int BODY_BUFFER_SIZE = 1024 * 1024 * 10;
    private static final String LOG_PREFIX = "OKPRFL";
    private static final String DELIMITER = "_";
    private static final Character HEADER_DELIMITER = ':';
    private static final Character SPACE = ' ';
    private static final String KEY_TAG = "TAG";
    private static final String KEY_VALUE = "VALUE";
    private static final String KEY_PARTS_COUNT = "PARTS_COUNT";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private final Handler mHandler;

    public LogDataTransfer() {
        final HandlerThread handlerThread = new HandlerThread("OkHttpProfiler", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mHandler = new LogBodyHandler(handlerThread.getLooper());
    }

    @Override
    public void sendRequest(String id, Request request) throws IOException {
        fastLog(id, MessageType.REQUEST_METHOD, request.method());
        String url = request.url().toString();
        fastLog(id, MessageType.REQUEST_URL, url);
        fastLog(id, MessageType.REQUEST_TIME, String.valueOf(System.currentTimeMillis()));

        final Request copy = request.newBuilder().build();
        final Buffer buffer = new Buffer();
        RequestBody body = copy.body();

        if (body != null) {
            MediaType type = body.contentType();
            if (type != null) {
                fastLog(id, MessageType.REQUEST_HEADER, CONTENT_TYPE + HEADER_DELIMITER + SPACE + type.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                fastLog(id, MessageType.REQUEST_HEADER, CONTENT_LENGTH + HEADER_DELIMITER + SPACE + contentLength);
            }
        }

        Headers headers = request.headers();
        if (headers != null) {
            for (String name : headers.names()) {
                //We have logged them before
                if (CONTENT_TYPE.equalsIgnoreCase(name) || CONTENT_LENGTH.equalsIgnoreCase(name)) continue;
                fastLog(id, MessageType.REQUEST_HEADER, name + HEADER_DELIMITER + SPACE + headers.get(name));
            }
        }

        if (body != null) {
            body.writeTo(buffer);
            largeLog(id, MessageType.REQUEST_BODY, buffer.readUtf8());
        }
    }

    @Override
    public void sendResponse(String id, Response response) throws IOException {
        ResponseBody responseBodyCopy = response.peekBody(BODY_BUFFER_SIZE);
        largeLog(id, MessageType.RESPONSE_BODY, responseBodyCopy.string());

        Headers headers = response.headers();
        logWithHandler(id, MessageType.RESPONSE_STATUS, String.valueOf(response.code()), 0);
        if (headers != null) {
            for (String name : headers.names()) {
                logWithHandler(id, MessageType.RESPONSE_HEADER, name + HEADER_DELIMITER + headers.get(name), 0);
            }
        }
    }

    @Override
    public void sendException(String id, Exception response) {
        logWithHandler(id, MessageType.RESPONSE_ERROR, response.getLocalizedMessage(), 0);
    }

    @Override
    public void sendDuration(String id, long duration) {
        logWithHandler(id, MessageType.RESPONSE_TIME, String.valueOf(duration), 0);
        logWithHandler(id, MessageType.RESPONSE_END, "-->", 0);
    }

    @SuppressLint("LogNotTimber")
    private void fastLog(String id, MessageType type, String message) {
        String tag = LOG_PREFIX + DELIMITER + id + DELIMITER + type.name;
        if (message != null) {
            Log.v(tag, message);
        }
    }

    private void logWithHandler(String id, MessageType type, String message, int partsCount) {
        Message handlerMessage = mHandler.obtainMessage();
        String tag = LOG_PREFIX + DELIMITER + id + DELIMITER + type.name;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TAG, tag);
        bundle.putString(KEY_VALUE, message);
        bundle.putInt(KEY_PARTS_COUNT, partsCount);
        handlerMessage.setData(bundle);
        mHandler.sendMessage(handlerMessage);
    }

    private void largeLog(String id, MessageType type, String content) {
        final int contentLength = content.length();
        if (content.length() > LOG_LENGTH) {
            final int parts = contentLength / LOG_LENGTH;
            for (int i = 0; i <= parts; i++) {
                final int start = i * LOG_LENGTH;
                int end = start + LOG_LENGTH;
                if (end > contentLength) {
                    end = contentLength;
                }
                logWithHandler(id, type, content.substring(start, end), parts);
            }
        } else {
            logWithHandler(id, type, content, 0);
        }
    }


    private class LogBodyHandler extends Handler {
        private LogBodyHandler(Looper looper) {
            super(looper);
        }

        @SuppressLint("LogNotTimber")
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                int partsCount = bundle.getInt(KEY_PARTS_COUNT, 0);
                if (partsCount > SLOW_DOWN_PARTS_AFTER) {
                    try {
                        Thread.sleep(5L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String data = bundle.getString(KEY_VALUE);
                String key = bundle.getString(KEY_TAG);
                if (data != null && key != null) {
                    Log.v(key, data);
                }
            }
        }
    }
}
