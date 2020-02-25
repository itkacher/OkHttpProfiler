package com.itkacher.okhttpprofiler;

import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import com.itkacher.okhttpprofiler.transfer.LogDataTransfer;
import com.itkacher.okhttpprofiler.transfer.DataTransfer;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;

/**
 * @author itkacher
 * @since 9/25/18
 */
public class OkHttpProfilerInterceptor implements Interceptor {

    private final DataTransfer dataTransfer = new LogDataTransfer();
    private final DateFormat format = new SimpleDateFormat("ddhhmmssSSS", Locale.US);
    private final AtomicLong previousTime = new AtomicLong();

    @NotNull
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

    /**
     * Generates unique string id via a day and time
     * Based on a current time.
     * @return string id
     */
    private synchronized String generateId() {
        long currentTime = Long.parseLong(format.format(new Date()));
        //Increase time if it the same, as previous (unique id)
        long previousTime = this.previousTime.get();
        if(currentTime <= previousTime) {
            currentTime = ++previousTime;
        }
        this.previousTime.set(currentTime);
        return Long.toString(currentTime, Character.MAX_RADIX);
    }
}
