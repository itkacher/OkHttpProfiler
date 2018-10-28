package com.itkacher.okhttpprofiler.transfer;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
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
    private static final String KEY_TAG = "TAG";
    private static final String KEY_VALUE = "VALUE";
    private final Handler mHandler;

    public LogDataTransfer() {
        final HandlerThread handlerThread = new HandlerThread("OkHttpProfiler", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mHandler = new LogBodyHandler(handlerThread.getLooper());
    }

    @Override
    public void sendRequest(String id, Request request) {
        fastLog(id, MessageType.REQUEST_METHOD, request.method());
        String url = request.url().toString();
        fastLog(id, MessageType.REQUEST_URL, url);
        fastLog(id, MessageType.REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
        Headers headers = request.headers();
        if (headers != null) {
            for (String name : headers.names()) {
                logWithHandler(id, MessageType.REQUEST_HEADER, name + ":" + headers.get(name));
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
        try {
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            largeLog(id, MessageType.RESPONSE_BODY, responseBodyCopy.string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Headers headers = response.headers();
        logWithHandler(id, MessageType.RESPONSE_STATUS, String.valueOf(response.code()));
        if (headers != null) {
            for (String name : headers.names()) {
                logWithHandler(id, MessageType.RESPONSE_HEADER, name + HEADER_DELIMITER + headers.get(name));
            }
        }
    }

    @Override
    public void sendException(String id, Exception response) {
        logWithHandler(id, MessageType.RESPONSE_ERROR, response.getLocalizedMessage());
    }

    @Override
    public void sendDuration(String id, long duration) {
        logWithHandler(id, MessageType.RESPONSE_TIME, String.valueOf(duration));
        logWithHandler(id, MessageType.RESPONSE_END, "-->");
    }

    private void fastLog(String id, MessageType type, String message) {
        String tag = LOG_PREFIX + DELIMITER + id + DELIMITER + type.name;
        Log.d(tag, message);
    }

    private void logWithHandler(String id, MessageType type, String message) {
        Message handlerMessage = mHandler.obtainMessage();
        String tag = LOG_PREFIX + DELIMITER + id + DELIMITER + type.name;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TAG, tag);
        bundle.putString(KEY_VALUE, message);
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
                logWithHandler(id, type, content.substring(start, end));
            }
        } else {
            logWithHandler(id, type, content);
        }
    }


    private class LogBodyHandler extends Handler {
        private LogBodyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle != null) {
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(bundle.getString(KEY_TAG), bundle.getString(KEY_VALUE));
            }
        }
    }
}
