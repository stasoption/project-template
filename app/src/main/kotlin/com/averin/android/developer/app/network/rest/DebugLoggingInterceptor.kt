package com.averin.android.developer.app.network.rest

import android.os.Environment
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class DebugLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val requestBody = request.body

        val connection = chain.connection()
        var requestStartMessage =
            ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        if (requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        logText(requestStartMessage)
        logHttpRequest(request, response)
        return response
    }

    private fun logHttpRequest(request: Request, response: Response? = null) {
        val requestBody = request.body
        val headers = request.headers
        if (requestBody != null) {
            // Request body headers are only present when installed as a network interceptor. When not
            // already present, force them to be included (if available) so their values are known.
            requestBody.contentType()?.let {
                if (headers["Content-Type"] == null) {
                    logText("Content-Type: $it")
                }
            }
            if (requestBody.contentLength() != -1L) {
                if (headers["Content-Length"] == null) {
                    logText("Content-Length: ${requestBody.contentLength()}")
                }
            }
        }
        for (i in 0 until headers.size) {
            logHeader(headers, i)
        }

        response?.let {
            logText("Response")
            val responseBody = response.body!!
            val contentLength = responseBody.contentLength()
            val responseHeaders = response.headers
            for (i in 0 until responseHeaders.size) {
                logHeader(responseHeaders, i)
            }
            logText(
                "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url}"
            )
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            var sourcebuffer = source.buffer

            var gzippedLength: Long? = null
            if ("gzip".equals(responseHeaders["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = sourcebuffer.size
                GzipSource(sourcebuffer.clone()).use { gzippedResponseBody ->
                    sourcebuffer = Buffer()
                    sourcebuffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType1 = responseBody.contentType()
            val charset1: Charset = contentType1?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

            if (contentLength != 0L) {
                logText("")
                logText(sourcebuffer.clone().readString(charset1))
            }

            if (gzippedLength != null) {
                logText("<-- END HTTP (${sourcebuffer.size}-byte, $gzippedLength-gzipped-byte body)")
            } else {
                logText("<-- END HTTP (${sourcebuffer.size}-byte body)")
            }
        }
        logText("*************************")
    }

    private fun logText(text: String?) {
        text?.let { Timber.e(it) }
        val logFilepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/logs.txt"
        val logFile = File(logFilepath)
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(URLDecoder.decode(text ?: "", "UTF-8"))
            buf.newLine()
            buf.close()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun logHeader(headers: Headers, i: Int) {
        if (i < headers.size) {
            val value = headers.value(i)
            logText(headers.name(i) + ": " + value)
        }
    }
}
