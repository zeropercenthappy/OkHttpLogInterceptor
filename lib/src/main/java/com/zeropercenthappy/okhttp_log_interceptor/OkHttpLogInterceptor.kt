package com.zeropercenthappy.okhttp_log_interceptor

import android.util.Log
import okhttp3.*
import okio.Buffer
import okio.GzipSource
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.DecimalFormat

class OkHttpLogInterceptor(private val logTag: String) : Interceptor {

    companion object {
        private const val textMimeType = "text/plain"
        private const val jsonMimeType = "application/json"
        private const val xmlMimeType = "text/xml"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val connection = chain.connection()
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1

        val request = chain.request()
        log("--> Request for ${request.url.scheme}://${request.url.host}")
        printRequest(request, protocol.toString())
        log("--> Request end")

        val response: Response
        try {
            response = chain.proceed(request)
            log("<-- Response from ${request.url.scheme}://${request.url.host}")
            printResponse(response, protocol.toString())
            log("<-- Response end")
        } catch (e: Exception) {
            log("<-- Response from ${request.url.scheme}://${request.url.host}")
            log("Error: ${e.localizedMessage}")
            log("<-- Response end")
            throw e
        }

        return response
    }

    private fun log(content: String) {
        Log.i(logTag, content)
    }

    private fun printRequest(request: Request, protocol: String) {
        // 请求行
        val query = request.url.encodedQuery
        log("${request.method} ${request.url.encodedPath}${if (query != null) "?$query" else ""} $protocol")
        // Headers
        logHeaders(request.headers)
        // Body
        when (val requestBody = request.body) {
            is FormBody -> {
                val formBodySb = StringBuilder()
                for (i in 0 until requestBody.size) {
                    formBodySb.append(requestBody.name(i))
                        .append("=")
                        .append(requestBody.value(i))
                        .append("&")

                }
                if (formBodySb.last() == '&') formBodySb.deleteCharAt(formBodySb.lastIndex)
                log(formBodySb.toString())
            }
            is MultipartBody -> {
                for (part in requestBody.parts) {
                    val partBody = part.body
                    val headers = part.headers ?: Headers.Builder().build()
                    logHeaders(headers)
                    val contentType = partBody.contentType()?.toString() ?: ""
                    if (isTextContentType(contentType)) {
                        log(readRequestBodyString(partBody))
                    } else {
                        log("(binary, size:${formatSize(partBody.contentLength())})")
                    }
                }
            }
            else -> {
                if (requestBody != null) {
                    val contentType = requestBody.contentType()?.toString() ?: ""
                    if (isTextContentType(contentType)) {
                        log(readRequestBodyString(requestBody))
                    } else {
                        log("(binary, size:${formatSize(requestBody.contentLength())})")
                    }
                }
            }
        }
    }

    private fun printResponse(response: Response, protocol: String) {
        // 状态行
        log("$protocol ${response.code} ${response.message}")
        // Headers
        logHeaders(response.headers)
        // Body
        val body = response.body ?: return
        val contentType = body.contentType()?.toString() ?: ""
        if (isTextContentType(contentType)) {
            log(readResponseBody(response))
        } else {
            log("(binary, size=${formatSize(body.contentLength())})")
        }
    }

    private fun logHeaders(headers: Headers) {
        for (header in headers) {
            log("${header.first}: ${header.second}")
        }
        log(" ")
    }

    private fun isTextContentType(contentType: String): Boolean {
        return contentType.isEmpty() ||
                contentType.contains(textMimeType) ||
                contentType.contains(jsonMimeType) ||
                contentType.contains(xmlMimeType)
    }

    private fun readRequestBodyString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readString(Charset.defaultCharset())
    }

    private fun readResponseBody(response: Response): String {
        val body = response.body ?: return ""
        val source = body.source()
        source.request(Long.MAX_VALUE)
        if ("gzip".equals(response.header("Content-Encoding"), ignoreCase = true)) {
            // 对 gzip 压缩过的数据进行解压后再使用
            val unCompressBuffer = Buffer()
            GzipSource(source.buffer.clone()).use { gzipSource ->
                unCompressBuffer.writeAll(gzipSource)
            }
            if (unCompressBuffer.size != 0L) {
                val contentType = body.contentType()
                val charset: Charset = contentType?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
                return unCompressBuffer.readString(charset)
            } else {
                return ""
            }
        } else {
            // 非 gzip 压缩过的数据，直接使用
            return source.buffer.clone().readString(Charset.defaultCharset())
        }
    }

    private fun formatSize(byte: Long): String {
        val sizeSB = StringBuilder()
        when (byte) {
            in 0L..1024L -> {
                sizeSB.append(formatDecimal(byte.toFloat(), 2)).append("Byte")
            }
            in 1024L..1024_000L -> {
                sizeSB.append(formatDecimal(byte / 1024f, 2)).append("KB")
            }
            in 1024_000L..1024_000_000L -> {
                sizeSB.append(formatDecimal(byte / 1024_000f, 2)).append("MB")
            }
            in 1024_000_000L..1024_000_000_000L -> {
                sizeSB.append(formatDecimal(byte / 1024_000_000f, 2)).append("GB")
            }
        }
        return sizeSB.toString()
    }

    @Suppress("SameParameterValue")
    private fun formatDecimal(content: Float, scaleNumber: Int): String {
        val ruleSB = StringBuilder("#.")
        for (i in 0 until scaleNumber) {
            ruleSB.append("0")
        }
        val decimalFormat = DecimalFormat(ruleSB.toString())
        decimalFormat.roundingMode = RoundingMode.HALF_UP
        val result = StringBuilder(decimalFormat.format(content))
        if (result[0] == '.') {
            result.insert(0, "0")
        }
        return result.toString()
    }
}