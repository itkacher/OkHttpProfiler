package com.localebro.okprofilerexample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.localebro.okhttpprofiler.settings.OkHttpProfilerSettingsActivity.Companion.getIntent
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var mClient: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //OkHttp Initialization
        val builder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(OkHttpProfilerInterceptor())
            }
        }
        mClient = builder.build()
        sendRequest()
        findViewById<View>(R.id.send_request).setOnClickListener { sendRequest() }

        //Retrofit Initialization (if needed)
        val retrofit = Retrofit.Builder()
            .client(mClient)
            .baseUrl(BASE_URL)
            .build()
    }

    private fun sendRequest() {
        val request: Request = Request.Builder()
            .url(JSON_URL)
            .get()
            .build()

        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @Suppress("unused")
            override fun onResponse(call: Call, response: Response) {
                try {
                    val unusedText = response.body?.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun openSettings() {
        startActivity(getIntent(applicationContext))
    }

    companion object {
        //    private static final String JSON_URL = "https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/large_random_json.json";
        private const val BASE_URL = "https://raw.githubusercontent.com"
        private const val JSON_URL =
            "https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/colors.json"
    }
}
