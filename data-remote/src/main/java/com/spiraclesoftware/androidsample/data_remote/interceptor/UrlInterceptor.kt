package com.spiraclesoftware.androidsample.data_remote.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Simplifies adding parameters to intercepted requests.
 *
 * Example of adding an api-key to requests:
 * ```
 * class AuthInterceptor : UrlInterceptor() {
 *     override fun HttpUrl.Builder.update(url: HttpUrl) {
 *         addQueryParameter("api-key", BuildConfig.API_KEY)
 *     }
 * }
 * ```
 */
abstract class UrlInterceptor : Interceptor {

    final override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        val newRequest = request.newBuilder()
            .url(
                url.newBuilder().apply { update(url) }.build()
            ).build()
        return chain.proceed(newRequest)
    }

    protected abstract fun HttpUrl.Builder.update(url: HttpUrl)

}
