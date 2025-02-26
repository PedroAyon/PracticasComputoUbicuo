package dev.pedroayon.pdm05b_abpj.data.remote

import dev.pedroayon.pdm05b_abpj.data.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("/v1/latest")
    suspend fun getLatestRates(
        @Query("base_currency") baseCurrency: String,
        @Query("currencies") currencies: String
    ): CurrencyResponse
}