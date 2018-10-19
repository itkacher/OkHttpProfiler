# OkHttpProfiler

For activating you need to include library to your android ``build.gradle`` file (module level)

``implementation 'com.itkacher.okhttpprofiler:okhttpprofiler:0.0.1'``
and add Interceptor to okHttpClient

Java

``OkHttpClient mClient = new OkHttpClient.Builder().addInterceptor( new OkHttpProfilerInterceptor() ).build();``
Kotlin

``val client = OkHttpClient.Builder().addInterceptor( OkHttpProfilerInterceptor() ).build()``

## Then install Android Studio / Intellij IDE plugin

https://plugins.jetbrains.com/plugin/11249-okhttp-profiler

Have fun!

![Screen1](https://github.com/itkacher/OkHttpProfiler/blob/master/screen1.png?raw=true)
![Screen2](https://github.com/itkacher/OkHttpProfiler/blob/master/screen2.png?raw=true)
