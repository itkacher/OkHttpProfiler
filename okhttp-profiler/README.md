# OkHttpProfiler Android Library

### Video Instructions on [NerdyThings Channel](https://youtu.be/KI_1-rUMjEI?si=EpXpMZncTNf53wxB&t=458 "YouTube.com")

### Developers Eugene Tkachenko && Hanna Tkachenko

## Important! Package migration!

To migrate your plugin just change old package to the:

```kotlin
    implementation("io.nerdythings:okhttp-profiler:1.1.1")
```

*And fix imports in the code*

in your code.

**Also the OkHttp client was excluded from okhttp-profiler, so feel free to include the latest version directly: [OkHttpClient](https://square.github.io/okhttp/ "OkHttp client")**

```kotlin
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
```

## End Important

The [OkHttp Profiler plugin](https://plugins.jetbrains.com/plugin/11249-okhttp-profiler "OkHttp Profiler") can show requests from the OkHttp library directly in the Android Studio tool window.
It supports the OkHttp v3 (http://square.github.io/okhttp/) and the Retrofit v2 (https://square.github.io/retrofit/)

You can **debug OkHttp request** or response headers, inspect the JSON as a tree, as a plain text etc. And you can easily **create a Java/Kotlin model from the data**.
Just click the right mouse button on a root element of the tree (or any other), choose Java or Kotlin, and select a folder for a new file in the project.

![Screen2](https://github.com/itkacher/OkHttpProfiler/blob/master/demo.png?raw=true)

---

## Installation

For installation, you need to include the library to your app (module level) build.gradle file
```kotlin
    implementation("io.nerdythings:okhttp-profiler:1.1.1")
```

and add Interceptor to okHttpClient in code
##### For OkHttp
###### Java

```java
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
     if (BuildConfig.DEBUG) {
         builder.addInterceptor(new OkHttpProfilerInterceptor());
     }   
    OkHttpClient client = builder.build(); 
```

###### Kotlin
```kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(OkHttpProfilerInterceptor() )
    }    
    val client = builder.build()
```

##### For Retrofit
###### Java

```java
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
     if (BuildConfig.DEBUG) {
         builder.addInterceptor(new OkHttpProfilerInterceptor());
     }   
    OkHttpClient client = builder.build(); 
    Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .build();
```

###### Kotlin
```kotlin
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        builder.addInterceptor( OkHttpProfilerInterceptor() )
    }    
    val client = builder.build()
    val retrofit = Retrofit.Builder()
            ......
            .client(client)
            .build()
```

##### For security reasons we recommend to enable OkHttpProfilerInterceptor only for DEBUG BUILDS!
Also Proguard will cut it out in the release build.

#### And then enable Android Studio plugin

https://plugins.jetbrains.com/plugin/11249-okhttp-profiler

![Screen2](https://github.com/itkacher/OkHttpProfiler/blob/master/plugin_install1.png?raw=true)

![Screen3](https://github.com/itkacher/OkHttpProfiler/blob/master/plugin_install2.png?raw=true)

![Screen1](https://github.com/itkacher/OkHttpProfiler/blob/master/screen1.png?raw=true)

![Screen4](https://github.com/itkacher/OkHttpProfiler/blob/master/screen2.png?raw=true)

**Have fun!**

## Source Codes
#### [OkHttp Profiler Plugin Sources](https://github.com/gektor650/OkHttpProfiler-AndroidStudio-Plugin "OkHttp Profiler Plugin Sources").
#### [OkHttp Profiler Android Library](https://github.com/itkacher/OkHttpProfiler "OkHttp Profiler Android Library").


#### Issue with the Android Studio version 4.1 solved in plugin v1.0.13+:
Unfortunately, Google changed realisation of one class but Intellij doesn't. I fixed it, but Intellij IDEA is no longer supported by the OkHttProfiler.

