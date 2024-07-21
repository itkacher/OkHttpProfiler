package io.nerdythings.modifier.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class OkHttpRequestModifierInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}