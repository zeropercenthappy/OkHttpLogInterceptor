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
    implementation 'com.github.zeropercenthappy:OkHttpLogInterceptor:1.6.1'
}
```

## Usage

### Kotlin

```kotlin
val logInterceptor = OkHttpLogInterceptor("yourOwnTag")
// add this interceptor
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logInterceptor)
    .build()
```

### Java

```java
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor("yourOwnTag");
// add this interceptor
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build();
```

## Effect

It will print as below:

```
== Request ==
POST /login http/1.1
 
name=guest&password=123456
== Response ==
http/1.1 200 
Server: Tengine/2.2.2
Date: Wed, 16 Dec 2020 06:59:01 GMT
Content-Type: application/json;charset=utf-8
Transfer-Encoding: chunked
Connection: keep-alive
Vary: Accept-Encoding
Set-Cookie: JSESSIONID=CC136BA58768DDD1122D53194438E6F2; Path=/; HttpOnly
 
{"code":200,"succeed":true}
```

