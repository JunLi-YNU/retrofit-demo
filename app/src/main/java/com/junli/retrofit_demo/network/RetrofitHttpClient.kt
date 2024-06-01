package com.junli.retrofit_demo.network

import android.util.Log
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.TlsVersion
import org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.Collections
import java.util.Date
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitHttpClient {
    // 创建信任管理器
    private val trustManagerFactory = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(KeyStore.getInstance(null))
        }

    private val trustManagers = trustManagerFactory.trustManagers

    private val sslContext: SSLContext = SSLContext.getInstance("TLS").apply {
        init(null, trustManagers,null)
    }

    // 创建SSLSocketFactory
    private val sslSocketFactory = sslContext.socketFactory

    // 创建自定义的ConnectionSpec
    private val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2) // 只允许TLS 1.2
        .cipherSuites(
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            // ... 其他允许的密码套件
        )
        .build()


    private const val BASE_URL = "http://192.168.2.253:8088/"
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .addInterceptor(HeaderInterceptor())
        .sslSocketFactory(sslSocketFactory, trustManagers[0] as X509TrustManager)
        .connectionSpecs(Collections.singletonList(spec))
        .build()

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofitBuilder.create(serviceClass)

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent", System.getProperty("http.agent") ?: "unknown")
            }.build()
            return chain.proceed(request)
        }
    }

    class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val t1 = System.nanoTime()
            Log.v(TAG, "Sending request: ${originalRequest.url()} \n ${originalRequest.headers()}")

            val response = chain.proceed(originalRequest)

            val t2 = System.nanoTime()
            Log.v(
                TAG,
                "Received response for  ${
                    response.request().url()
                } in ${(t2 - t1) / 1e6} ms\n${response.headers()}"
            )
            return response
        }

        companion object {
            const val TAG = "LoggingInterceptor"
        }
    }
}

