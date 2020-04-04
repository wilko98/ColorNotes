package com.infinit.colornotes.di

import com.infinit.colornotes.utils.PrefManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(val prefManager: PrefManager) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val requestBuilder = chain.request().newBuilder().addHeader("Token",prefManager.getToken())
        chain.proceed(requestBuilder.build())
    }
}