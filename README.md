# OkHttpProfiler

OkHttp Profiler plugin can show request from okhttp library directly in your Android Studio toolwindow.
It supports okhttp v3 (http://square.github.io/okhttp/) or Retrofit v2 (https://square.github.io/retrofit/)

![Screen1](https://github.com/itkacher/OkHttpProfiler/blob/master/screen1.png?raw=true)

---
[ ![Download](https://api.bintray.com/packages/itkacher/okhttpprofiler/com.itkacher.okhttpprofiler/images/download.svg) ](https://bintray.com/itkacher/okhttpprofiler/com.itkacher.okhttpprofiler/_latestVersion)
 
For installation you need to include library to your app build.gradle file

    implementation 'com.itkacher.okhttpprofiler:okhttpprofiler:1.0.2'

and add Interceptor to okHttpClient in code
##### For OkHttp
###### Java
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
     if (BuildConfig.DEBUG) {
         builder.addInterceptor(new OkHttpProfilerInterceptor());
     }   
    OkHttpClient client = builder.build(); 

###### Kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(OkHttpProfilerInterceptor() )
    }    
    val client = builder.build()
    
##### For Retrofit
###### Java
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
     if (BuildConfig.DEBUG) {
         builder.addInterceptor(new OkHttpProfilerInterceptor());
     }   
    OkHttpClient client = builder.build(); 
    Retrofit retrofit = new Retrofit.Builder()
                ......
                .client(client)
                .build();
                
                
###### Kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor( OkHttpProfilerInterceptor() )
    }    
    val client = builder.build()
    val retrofit = Retrofit.Builder()
            ......
            .client(client)
            .build()

##### For security reasons we recommend to enable OkHttpProfilerInterceptor only for DEBUG BUILDS! 
also Proguard will cut it out in release build.

#### And then enable Android Studio plugin

https://plugins.jetbrains.com/plugin/11249-okhttp-profiler

![Screen2](https://github.com/itkacher/OkHttpProfiler/blob/master/plugin_install1.png?raw=true)

![Screen3](https://github.com/itkacher/OkHttpProfiler/blob/master/plugin_install2.png?raw=true)

![Screen4](https://github.com/itkacher/OkHttpProfiler/blob/master/screen2.png?raw=true)

## Have fun!
