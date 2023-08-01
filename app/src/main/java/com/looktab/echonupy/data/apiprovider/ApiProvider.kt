package com.looktab.echonupy.data.apiprovider

import com.looktab.echonupy.data.api.NftService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {
    const val URL = "https://seoul-web3-admin.vercel.app"

    fun provideNft(): NftService = getRetrofitBuild.create(NftService::class.java)

    private var clientSignIn = OkHttpClient.Builder()
        .connectTimeout(1000, TimeUnit.SECONDS)
        .readTimeout(1000, TimeUnit.SECONDS)
        .writeTimeout(1000, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder().apply {
            }.build()
            chain.proceed(newRequest)
        }.build()

    private val getRetrofitBuild =
        Retrofit
            .Builder()
            .baseUrl(URL)
            .client(clientSignIn)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}