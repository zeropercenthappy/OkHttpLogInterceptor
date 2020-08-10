# OkHttpLogInterceptor
轻量且实用的OkHttp日志拦截器

支持打印`url`、`请求方法`、`header`、`url参数`、`body`、`multipart`以及`响应结果`

---

## 下载

### 步骤 1.

添加以下配置到项目根目录位置的build.gradle：

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### 步骤 2.

在Module目录下的build.gradle文件内添加依赖：

```groovy
dependencies {
    implementation 'com.github.zeropercenthappy:OkHttpLogInterceptor:1.5'
}
```

## 用法

### Kotlin

```kotlin
// 使用默认的tag：OkHttp
val logInterceptor = OkHttpLogInterceptor()
// 也可以使用自定义的tag
val logInterceptor = OkHttpLogInterceptor("myTag")
// 添加该拦截器
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logInterceptor)
    .build()
```

### Java

```java
// 使用默认的tag：OkHttp
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor();
// 也可以使用自定义的tag
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor("myTag");
// 添加该拦截器
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build();
```

## 效果

请求发起后会根据请求情况打印以下日志：

```
==Request==
url: https://www.xxxxx.com/xx?xxx=xxx
method: POST
header: XXXXX=XXXXXXXXXX
url parameter: xxx=xxx
form body: xxx=xxx
multipart body: xxx=xxx
multipart body: xxx={binary}, size=xxxKB
json body: {"xxx":"xxx"}
==Response==
response: {"xxx":"xxx"}
response fail: Unable to resolve host "www.xxxxx.com": No address associated with hostname
```

