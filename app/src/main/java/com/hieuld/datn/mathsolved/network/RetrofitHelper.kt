package com.hieuld.datn.mathsolved.network

import android.content.Context
import androidx.annotation.StringRes
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private var BASE_URL = "https://gsmdevaaaaa.com"

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    fun getInstanceAI(): Retrofit {
        val okHttpClient = createOkHttpClient(AIServiceInterceptor)
            .newBuilder()
            .callTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .build()
        return createRetrofit(okHttpClient)
    }

    val apiServiceAIAnywhere: RetrofitService = getInstanceAI().create(RetrofitService::class.java)

    private fun Context.getAppString(@StringRes resId: Int): String {
        return resources.getString(resId)
    }
}