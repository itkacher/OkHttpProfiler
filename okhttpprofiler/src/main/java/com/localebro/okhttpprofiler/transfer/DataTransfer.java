package com.localebro.okhttpprofiler.transfer;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public interface DataTransfer {
    void sendRequest(String id, Request request) throws IOException;

    void sendResponse(String id, Response response) throws IOException;

    void sendException(String id, Exception response);

    void sendDuration(String id, long duration);
}
