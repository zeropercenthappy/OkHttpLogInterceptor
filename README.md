# OkHttpLogInterceptor
A light weight and practical okhttp log interceptor, support to print `url`, `method`, `url parameter`, `body parameter`, `multipart body`, `json body`, and `response`.

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

Add the dependency:

```groovy
dependencies {
    implementation 'com.github.zeropercenthappy:OkHttpLogInterceptor:1.0'
}
```

## Usage

### Kotlin

```kotlin
// the default tag is OkHttp
val logInterceptor = OkHttpLogInterceptor()
// or you can custom your own tag
val logInterceptor = OkHttpLogInterceptor("myTag")
// add this interceptor
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logInterceptor)
    .build()
```

### Java

```java
// the default tag is OkHttp
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor();
// or you can custom your own tag
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor("myTag");
// add this interceptor
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build();
```

## Effect

It will print as below:

```
===
url:https://www.xxxxx.com/xx?xxx=xxx
method: POST
header: XXXXX=XXXXXXXXXX
url parameter: xxx=xxx
form body: xxx=xxx
multipart body: xxx={binary},size=xxxKB
json body: {"xxx:xxx"}
response: {"xxx":"xxx"}
===
```

