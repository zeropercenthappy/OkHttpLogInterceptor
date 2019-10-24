package com.zeropercenthappy.okhttploginterceptor

import android.text.TextUtils
import android.util.Log
import com.zeropercenthappy.utilslibrary.utils.NumberUtils
import okhttp3.*
import okio.Buffer
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

class OkHttpLogInterceptor(private val logTag: String? = DEFAULT_LOG_TAG) : Interceptor {

    companion object {
        private const val DEFAULT_LOG_TAG = "OkHttp"
        private const val PART_VALUE_REGEX = "(?<=name=\").*?(?=\")"
        private val VALUE_PATTERN = Pattern.compile(PART_VALUE_REGEX)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        log("===")

        log("url: ${request.url.toUrl()}")

        log("method: ${request.method}")

        logHeader(request.headers)

        logParameter(request.url)

        logBody(request.body)

        val response: Response
        try {
            response = chain.proceed(request)
            logResponse(response)
        } catch (e: Exception) {
            e.printStackTrace()
            log("response fail: ${e.localizedMessage}")
            throw e
        }

        log("===")

        return response
    }

    private fun logHeader(headers: Headers) {
        for (i in 0 until headers.size) {
            log("header: ${headers.name(i)}=${headers.value(i)}")
        }
    }

    private fun logParameter(httpUrl: HttpUrl) {
        for (name in httpUrl.queryParameterNames) {
            for (value in httpUrl.queryParameterValues(name)) {
                log("url parameter: $name=$value")
            }
        }
    }

    private fun logBody(requestBody: RequestBody?) {
        if (requestBody == null) {
            return
        }
        when (requestBody) {
            is FormBody -> {
                for (i in 0 until requestBody.size) {
                    log("form body: ${requestBody.name(i)}=${requestBody.value(i)}")
                }
            }
            is MultipartBody -> {
                for (part in requestBody.parts) {
                    val size = part.headers?.size ?: 0
                    for (i in 0 until size) {
                        if (TextUtils.equals(part.headers?.name(i), "Content-Disposition")) {
                            val key = matchMultipartBodyKey(part.headers?.value(i) ?: "")
                            val partBody = part.body
                            if (TextUtils.equals(partBody.contentType()?.type, "text")) {
                                log("multipart body: $key=${readRequestBodyString(partBody)}")
                            } else {
                                log("multipart body: $key={binary},size=${formatSize(partBody.contentLength())}")
                            }
                        }
                    }
                }
            }
            else -> {
                if (TextUtils.equals(requestBody.contentType()?.subtype?.toLowerCase(Locale.US), "json")) {
                    log("json body: ${readRequestBodyString(requestBody)}")
                }
            }
        }
    }

    private fun logResponse(response: Response?) {
        if (response == null) {
            return
        }
        val body = response.body ?: return
        if (TextUtils.equals(body.contentType()?.type, "application")) {
            log("response: ${readResponseBody(body)}")
        } else {
            log("response: ${body.contentType()?.type}/${body.contentType()?.subtype}, size=${formatSize(body.contentLength())}")
        }
    }

    private fun log(content: String) {
        Log.i(logTag, content)
    }

    private fun readRequestBodyString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readString(Charset.defaultCharset())
    }

    private fun readResponseBody(responseBody: ResponseBody): String {
        val source = responseBody.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer.clone()
        return buffer.readString(Charset.defaultCharset())
    }

    private fun matchMultipartBodyKey(content: String): String {
        return try {
            val matcher = VALUE_PATTERN.matcher(content)
            matcher.find()
            matcher.group()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun formatSize(byte: Long): String {
        val sizeSB = StringBuilder()
        when (byte) {
            in 0L..1024L -> {
                sizeSB.append(NumberUtils.formatDecimal(byte.toFloat(), 2)).append("Byte")
            }
            in 1025L..1024000L -> {
                sizeSB.append(NumberUtils.formatDecimal(byte / 1024f, 2)).append("KB")
            }
            in 1025000L..1024000000L -> {
                sizeSB.append(NumberUtils.formatDecimal(byte / 1024000f, 2)).append("MB")
            }
            in 1025000000L..1024000000000L -> {
                sizeSB.append(NumberUtils.formatDecimal(byte / 1024000000f, 2)).append("GB")
            }
        }
        return sizeSB.toString()
    }
}