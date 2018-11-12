package com.itkacher.okhttpprofiler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.itkacher.okhttpprofiler.transfer.LogDataTransfer;
import com.itkacher.okhttpprofiler.transfer.DataTransfer;

import okhttp3.*;

/**
 * @author itkacher
 * @since 9/25/18
 */
public class OkHttpProfilerInterceptor implements Interceptor {

    private final DataTransfer dataTransfer = new LogDataTransfer();
    private final DateFormat format = new SimpleDateFormat("ddhhmmssSSS", Locale.US);

    @Override
    public Response intercept(Chain chain) throws IOException {
        String id = generateId();
        long startTime = System.currentTimeMillis();
        dataTransfer.sendRequest(id, chain.request());
        try {
            Response response = chain.proceed(chain.request());
            dataTransfer.sendResponse(id, response);
            dataTransfer.sendDuration(id, System.currentTimeMillis() - startTime);
            return response;
        } catch (Exception e) {
            dataTransfer.sendException(id, e);
            dataTransfer.sendDuration(id, System.currentTimeMillis() - startTime);
            throw e;
        }
    }

    private String generateId() {
        long timeAndDay = Long.parseLong(format.format(new Date()));
        return Long.toString(timeAndDay, Character.MAX_RADIX);
    }
}
