# OkHttpProfiler

The OkHttp Profiler plugin can show request from the OkHttp library directly in the Android Studio tool window.
It supports okhttp v3 (http://square.github.io/okhttp/) or Retrofit v2 (https://square.github.io/retrofit/)

You can explore request/response headers, inspect the JSON as a tree, as a plain text etc. And you can easily **create a Java/Kotlin model from the data**. 
Just click the right mouse button on a root element of the tree (or any other), choose Java or Kotlin, and select a folder for a new file in the project.

![Screen1](https://github.com/itkacher/OkHttpProfiler/blob/master/screen1.png?raw=true)

**Youtube preview (click for watching)**

[![OkHttpProfiler](http://img.youtube.com/vi/OfYzSBMJPfs/0.jpg)](https://www.youtube.com/watch?v=OfYzSBMJPfs "OkHttpProfiler")

---
[ ![Download](https://api.bintray.com/packages/itkacher/okhttpprofiler/com.itkacher.okhttpprofiler/images/download.svg) ](https://bintray.com/itkacher/okhttpprofiler/com.itkacher.okhttpprofiler/_latestVersion)
 
## Installation 

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

**Have fun!**
