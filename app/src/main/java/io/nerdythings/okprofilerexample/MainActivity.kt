package io.nerdythings.okprofilerexample

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.localebro.okprofilerexample.BuildConfig
import com.localebro.okprofilerexample.R
import io.nerdythings.okhttp.modifier.interceptor.OkHttpRequestModifierInterceptor
import io.nerdythings.okhttp.modifier.settings.OkHttpProfilerSettingsActivity
import io.nerdythings.okhttp.profiler.OkHttpProfilerInterceptor
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
        setContent {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Button(onClick = ::sendRequest) {
                    Text(text = stringResource(id = R.string.send_request))
                }
                Button(onClick = ::openSettings) {
                    Text(text = stringResource(id = R.string.open_modifier))
                }
            }
        }

        //OkHttp Initialization
        val builder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(OkHttpProfilerInterceptor())
                addInterceptor(OkHttpRequestModifierInterceptor(applicationContext))
            }
        }
        mClient = builder.build()
        sendRequest()
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
        startActivity(OkHttpProfilerSettingsActivity.getIntent(applicationContext))
    }

    companion object {
        //    private static final String JSON_URL = "https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/large_random_json.json";
        private const val BASE_URL = "https://raw.githubusercontent.com"
        private const val JSON_URL =
            "https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/colors.json"
    }
}
