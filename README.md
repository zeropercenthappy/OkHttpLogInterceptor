# OkHttpLogInterceptor [简体中文](https://github.com/zeropercenthappy/OkHttpLogInterceptor/blob/master/README_CN.md)

A light weight and practical OkHttp log interceptor

---

## Download

### Step 1.

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2.

Add the dependency in your module's build.gradle:

```groovy
dependencies {
    implementation 'com.github.zeropercenthappy:OkHttpLogInterceptor:1.6.5'
}
```

## Usage

### Kotlin

```kotlin
val logInterceptor = OkHttpLogInterceptor("yourOwnTag")
// add this interceptor
val okHttpClient = OkHttpClient.Builder()
	.addNetworkInterceptor(logInterceptor)
	// or
    // .addInterceptor(logInterceptor)
    .build()
```

### Java

```java
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor("yourOwnTag");
// add this interceptor
OkHttpClient okHttpClient = new OkHttpClient.Builder()
    	.addNetworkInterceptor(logInterceptor)
    	// or
        // .addInterceptor(logInterceptor)
        .build();
```

## Effect

It will print as below:

```
--> Request for htpps://api.xxx.com
POST /login http/1.1
Content-Type: application/x-www-form-urlencoded
Content-Length: 56
Host: api.xxx.com
Connection: Keep-Alive
Accept-Encoding: gzip
Cookie: JSESSIONID=9CB615425A54C2BB9E66D43509360D1A
User-Agent: okhttp/4.9.0
 
name=guest&password=123456&extraParamKey=extraParamValue
--> Request end
<-- Response from htpps://api.xxx.com
http/1.1 200 
Server: Tengine/2.2.2
Date: Wed, 16 Dec 2020 06:59:01 GMT
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Connection: keep-alive
Vary: Accept-Encoding
Set-Cookie: JSESSIONID=CC136BA58768DDD1122D53194438E6F2; Path=/; HttpOnly
 
{"code":200,"succeed":true}
<-- Response end
```

## Other

​	If using `addInterceptor()`method to add this log interceptor to `Application Interceptor`, the log print will not inclue standard headers of request, because these headers will be added during OkHttp core flow which is beyond `Application Interceptor`.

​	Alternative, you can use `addNetworkInterceptor()` method to add this log interceptor to `Network Interceptor`. In this case you can see every thing is included in the log print. But if cache is enable and the cache of a request is exist, log interceptor will not print anything. Becasuse cache flow and network flow are exclusive.

