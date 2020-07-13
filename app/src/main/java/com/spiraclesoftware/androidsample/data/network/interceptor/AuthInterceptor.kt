package com.spiraclesoftware.androidsample.data.network.interceptor

import okhttp3.HttpUrl

class AuthInterceptor : UrlInterceptor() {
    override fun HttpUrl.Builder.update(url: HttpUrl) {
        //addQueryParameter("api-key", BuildConfig.API_KEY)
    }
}
