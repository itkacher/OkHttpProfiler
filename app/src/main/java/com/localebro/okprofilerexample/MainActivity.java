package com.localebro.okprofilerexample;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    //    private static final String JSON_URL = "https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/large_random_json.json";
    private static final String JSON_URL = "https://raw.githubusercontent.com/itkacher/OkHttpProfiler/master/colors.json";
    private OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OkHttp Initialization
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        mClient = builder.build();
        sendRequest();
        findViewById(R.id.send_request).setOnClickListener(v -> sendRequest());

        //Retrofit Initialization (if needed)
        Retrofit retrofit = new Retrofit.Builder()
            .client(mClient)
                .build();
    }

    private void sendRequest() {
        Request request = new Request.Builder()
                .url(JSON_URL)
                .get()
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @SuppressWarnings("unused")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (response.body() != null) {
                        String unusedText = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
