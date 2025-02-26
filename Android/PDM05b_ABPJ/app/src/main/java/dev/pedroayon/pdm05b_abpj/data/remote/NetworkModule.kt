package dev.pedroayon.pdm05b_abpj.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.freecurrencyapi.com/"
    private const val API_KEY = "YOUR_API_KEY_HERE"

    val currencyApiService: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val url = chain.request().url
                            .newBuilder()
                            .addQueryParameter("apikey", API_KEY)
                            .build()
                        chain.proceed(chain.request().newBuilder().url(url).build())
                    }
                    .build()
            )
            .build()
            .create(CurrencyApiService::class.java)
    }
}
