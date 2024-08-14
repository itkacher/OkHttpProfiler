# OkHttp Request Modifier library 

### Modify OkHttp Request easily!

### Video Instructions On [Youtube](https://www.youtube.com/watch?v=TrGYjPizNAQ "YouTube.com")
[![Video Instructions on](https://github.com/itkacher/OkHttpProfiler/blob/master/images/okhttp-request-modifier.jpg?raw=true)](https://www.youtube.com/watch?v=TrGYjPizNAQ "YouTube.com")

Request Modifier is a new Android library designed to provide developers with an easy way to
customize HTTP responses.
By adding this library into your project, you gain the ability to modify response bodies and
response codes dynamically.

| <img src="https://github.com/itkacher/OkHttpProfiler/blob/master/request_modifiers_activity.png?raw=true" width=250> | <img src="https://github.com/itkacher/OkHttpProfiler/blob/master/request_modifiers_add_new_modifier.png?raw=true" width=250> |
|----------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|

## Installation

For installation, you need to include the library to your app build.gradle file

```kotlin
    releaseImplementation("io.nerdythings:okhttp-requests-modifier-no-op:1.0.2")
    debugImplementation("'io.nerdythings:okhttp-requests-modifier:1.0.2")
```

and add an Interceptor to the OkHttpClient in the code

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

##### Call activity in your app

Call `OkHttpProfilerSettingsActivity` from your code

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContent { 
             Column(
                verticalArrangement = Arrangement.Center, 
                horizontalAlignment = Alignment.CenterHorizontally, 
                modifier = Modifier.fillMaxSize(),
              ) {
                Button(onClick = ::openSettings) {
                   Text(text = stringResource(id = R.string.open_modifier))
                } 
             }
         }
    }

    private fun openSettings() {
        startActivity(OkHttpProfilerSettingsActivity.getIntent(applicationContext))
    }
}
```

##### For security reasons we recommend to enable OkHttpProfilerInterceptor only for DEBUG BUILDS!

Also Proguard will cut it out in the release build.

## Customization Capabilities

With the Request Modifier, you can adjust the response body and response code for your HTTP
requests.
Additionally, the library supports the use of regular expressions (regex) to match and modify
multiple requests at once, offering a powerful and flexible way to handle different scenarios.

Important: if you override request, it won't make a real call, it'll immediately return your custom
data.

## Usage

To define custom responses, follow these steps:

1. Add the library to your project and set up the custom interceptor as shown above.
2. Access the built-in Activity from your debug menu, which provides a user-friendly interface for
   managing your requests.
3. Through this Activity, you can easily add and modify your requests, specifying the desired
   response bodies and response codes.
   Call `OkHttpProfilerSettingsActivity` from your code

```kotlin
class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {
         Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
         ) {
            Button(onClick = ::openSettings) {
               Text(text = stringResource(id = R.string.open_modifier))
            }
         }
      }
   }

   private fun openSettings() {
      startActivity(OkHttpProfilerSettingsActivity.getIntent(applicationContext))
   }
}
```

This intuitive UI makes it straightforward for developers to define and manage custom responses
without diving deep into the code, enhancing productivity and debugging efficiency.
