
# This repository has code of OkHttp Profiler and OkHttp Request Modifier Android Libraries

[OkHttp Profiler Android Library](https://github.com/itkacher/OkHttpProfiler/tree/master?tab=readme-ov-file#okhttp-profiler-android-library "OkHttp Profiler")

[OkHttp Request Modifier Android Library](https://github.com/itkacher/OkHttpProfiler/tree/master?tab=readme-ov-file#okhttp-request-modifier-android-library "OkHttp Request Modifier")

## OkHttp Profiler Android Library

### Video Instructions On [Youtube](https://youtu.be/KI_1-rUMjEI?si=EpXpMZncTNf53wxB&t=458 "YouTube.com")
[![Video Instructions on](https://github.com/itkacher/OkHttpProfiler/blob/master/demo.png?raw=true)](https://youtu.be/KI_1-rUMjEI?si=EpXpMZncTNf53wxB&t=458 "YouTube.com")

1. Install AndroidStudio Plugin plugin:
[OkHttp Profiler plugin](https://plugins.jetbrains.com/plugin/11249-okhttp-profiler "OkHttp Profiler")
2. Add library to app `build.gradle` file (module level):
```kotlin
implementation("io.nerdythings:okhttp-profiler:1.1.1")
```
3. Add interceptors to your OkHttp client:

###### OkHttpClient
```kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(OkHttpProfilerInterceptor() )
    }    
    val client = builder.build()
```

##### For Retrofit
```kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor( OkHttpProfilerInterceptor() )
    }    
    val client = builder.build()
    val retrofit = Retrofit.Builder()
            .client(client)
            .build()
```

#### Full Readme:

[OkHttp Profiler library](https://github.com/itkacher/OkHttpProfiler/tree/master/okhttp-profiler "OkHttp Profiler Readme")

## OkHttp Request Modifier Android Library

Installation:

1. Add libraries to app `build.gradle` file (module level):
```kotlin
    releaseImplementation("io.nerdythings:okhttp-requests-modifier-no-op:1.1.0")
    debugImplementation("io.nerdythings:okhttp-requests-modifier:1.0.0")
```
2. Add interceptors to your OkHttp client:

##### For OkHttp
```kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(OkHttpRequestModifierInterceptor(applicationContext))
    }    
    val client = builder.build()
```

##### For Retrofit
```kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(OkHttpRequestModifierInterceptor(applicationContext))
    }    
    val client = builder.build()
    val retrofit = Retrofit.Builder()
            .client(client)
            .build()
```

3. Call `OkHttpProfilerSettingsActivity` from your code

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        findViewById<View>(R.id.open_settings).setOnClickListener { openSettings() }
    }

    private fun openSettings() {
        startActivity(OkHttpProfilerSettingsActivity.getIntent(applicationContext))
    }
}
```

#### Full Readme:

[OkHttp Request Modifier library](https://github.com/itkacher/OkHttpProfiler/tree/master/okhttp-requests-modifier "OkHttp Request Modifier")