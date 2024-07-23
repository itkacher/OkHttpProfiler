package io.nerdythings.okhttp.modifier.interceptor

import android.content.Context
import io.nerdythings.okhttp.modifier.data.DataModifier
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import java.io.IOException

class OkHttpRequestModifierInterceptor(context: Context) : Interceptor {
    private val dataModifier = DataModifier(context)

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        try {
            return dataModifier.modifyResponse(chain.request()) {
                chain.proceed(chain.request())
            }
        } catch (e: Exception) {
            throw e
        }
    }
}