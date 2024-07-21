package com.localebro.okhttpprofiler

import com.localebro.okhttpprofiler.transfer.DataTransfer
import com.localebro.okhttpprofiler.transfer.LogDataTransfer
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicLong

/**
 * @author itkacher
 * @since 9/25/18
 */
class OkHttpProfilerInterceptor : Interceptor {
    private val dataTransfer: DataTransfer = LogDataTransfer()
    private val format: DateFormat = SimpleDateFormat("ddhhmmssSSS", Locale.US)
    private val previousTime = AtomicLong()

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val id = generateId()
        val startTime = System.currentTimeMillis()
        dataTransfer.sendRequest(id, chain.request())
        try {
            val response = chain.proceed(chain.request())
            dataTransfer.sendResponse(id, response)
            dataTransfer.sendDuration(id, System.currentTimeMillis() - startTime)
            return response
        } catch (e: Exception) {
            dataTransfer.sendException(id, e)
            dataTransfer.sendDuration(id, System.currentTimeMillis() - startTime)
            throw e
        }
    }

    /**
     * Generates unique string id via a day and time
     * Based on a current time.
     * @return string id
     */
    @Synchronized
    private fun generateId(): String {
        var currentTime = format.format(Date()).toLong()
        //Increase time if it the same, as previous (unique id)
        var previousTime = previousTime.get()
        if (currentTime <= previousTime) {
            currentTime = ++previousTime
        }
        this.previousTime.set(currentTime)
        return currentTime.toString(Character.MAX_RADIX.coerceIn(2, 36))
    }
}
