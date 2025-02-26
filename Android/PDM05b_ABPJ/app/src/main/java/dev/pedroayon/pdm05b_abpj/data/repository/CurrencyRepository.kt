package dev.pedroayon.pdm05b_abpj.data.repository

import dev.pedroayon.pdm05b_abpj.data.remote.CurrencyApiService

class CurrencyRepository(private val apiService: CurrencyApiService) {
    suspend fun getExchangeRate(base: String, target: String): Double {
        val response = apiService.getLatestRates(base, target)
        return response.data[target] ?: throw Exception("Rate not available")
    }
}