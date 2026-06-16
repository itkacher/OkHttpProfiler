package io.nerdythings.okprofilerexample

import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.localebro.okprofilerexample.BuildConfig
import com.localebro.okprofilerexample.R
import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.nerdythings.okhttp.modifier.interceptor.OkHttpRequestModifierInterceptor
import io.nerdythings.okhttp.modifier.settings.OkHttpProfilerSettingsActivity
import io.nerdythings.okhttp.profiler.OkHttpProfilerInterceptor
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var mClient: OkHttpClient

    val loggingPlugin = createClientPlugin("LoggingPlugin") {

        onResponse { response ->
            println("LSS_TK Response: ${response.status}")
            println("LSS_TK Response headers: ${response.headers}")
            println("LSS_TK Response body: ${response.bodyAsText()}")
        }
    }

    private val client = HttpClient {
        install(loggingPlugin)
    }

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
//        val request: Request = Request.Builder()
//            .url(JSON_URL)
//            .get()
//            .build()
        println("LSS_TK Sending request to: $JSON_URL")

        lifecycleScope.launch {
            client.get(JSON_URL)
        }

//        mClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//            }
//
//            @Suppress("unused")
//            override fun onResponse(call: Call, response: Response) {
//                try {
//                    val unusedText = response.body?.toString()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        })
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
