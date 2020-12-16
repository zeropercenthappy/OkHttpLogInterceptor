# OkHttpLogInterceptor
轻量且实用的OkHttp日志拦截器

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
    implementation 'com.github.zeropercenthappy:OkHttpLogInterceptor:1.6.1'
}
```

## 用法

### Kotlin

```kotlin
val logInterceptor = OkHttpLogInterceptor("yourOwnTag")
// 添加该拦截器
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logInterceptor)
    .build()
```

### Java

```java
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor("yourOwnTag");
// 添加该拦截器
OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .build();
```

## 效果

请求发起后会根据请求情况打印以下日志：

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

