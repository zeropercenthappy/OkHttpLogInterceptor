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
    implementation 'com.github.zeropercenthappy:OkHttpLogInterceptor:1.6.5'
}
```

## 用法

### Kotlin

```kotlin
val logInterceptor = OkHttpLogInterceptor("yourOwnTag")
// 添加该拦截器
val okHttpClient = OkHttpClient.Builder()
	.addNetworkInterceptor(logInterceptor)
	// 或
    // .addInterceptor(logInterceptor)
    .build()
```

### Java

```java
OkHttpLogInterceptor logInterceptor = new OkHttpLogInterceptor("yourOwnTag");
// 添加该拦截器
OkHttpClient okHttpClient = new OkHttpClient.Builder()
    	.addNetworkInterceptor(logInterceptor)
    	// 或
        // .addInterceptor(logInterceptor)
        .build();
```

## 效果

请求发起后会根据请求情况打印以下日志：

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

## 其它

​	如果使用`addInterceptor()`方法将日志拦截器添加到`应用层拦截`中，打印的请求日志中将不会包含一些标准请求头信息，因为这些请求头信息是在OkHttp的核心处理流程中被添加的，该流程位于`应用层拦截`流程之后。

​	可以使用`addNetworkInterceptor()`将日志拦截器添加到`网络层拦截`中，在这种情况下打印的请求日志将包含所有报文信息。但如果配置了缓存，并且该请求对应的缓存结果存在时，日志拦截器将不会打印任何东西，因为缓存流程与Okhttp核心处理流程是互斥的两个流程。