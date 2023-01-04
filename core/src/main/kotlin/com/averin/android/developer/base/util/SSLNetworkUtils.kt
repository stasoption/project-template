package com.averin.android.developer.base.util

import okhttp3.OkHttpClient
import timber.log.Timber
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSLNetworkUtils : HostnameVerifier {

    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }

    private val disabledX509TrustManager: X509TrustManager
        get() = object : X509TrustManager {

            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    private val disabledSslSocketFactory: SSLSocketFactory
        @Throws(KeyManagementException::class, NoSuchAlgorithmException::class)
        get() {
            val context = SSLContext.getInstance("TLS")
            context.init(null, arrayOf<TrustManager>(disabledX509TrustManager), SecureRandom())
            return context.socketFactory
        }

    fun disableSSLValidation(builder: OkHttpClient.Builder) {
        try {
            builder.sslSocketFactory(disabledSslSocketFactory, disabledX509TrustManager)
            builder.hostnameVerifier(this)
        } catch (e: NoSuchAlgorithmException) {
            Timber.e(e)
        } catch (e: KeyManagementException) {
            Timber.e(e)
        }
    }

    @JvmStatic
    fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            val builder = OkHttpClient.Builder()
            disableSSLValidation(builder)
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
