package com.itkacher.okhttpprofiler.transfer;

import okhttp3.Request;
import okhttp3.Response;

public interface DataTransfer {
    void sendRequest(String id, Request request);
    void sendResponse(String id, Response response);
    void sendException(String id, Exception response);
    void sendDuration(String id, long duration);
}
