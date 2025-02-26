package dev.pedroayon.pdm05b_abpj.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pedroayon.pdm05b_abpj.data.repository.CurrencyRepository
import dev.pedroayon.pdm05b_abpj.util.Constants
import kotlinx.coroutines.launch

class CurrencyViewModel(repository: CurrencyRepository) : ViewModel() {
    private var amount: Double = 0.0
    private val repo = repository

    var fromAmount by mutableStateOf("1")
    var toAmount by mutableStateOf("")
    var fromCurrency by mutableStateOf(Constants.currencyList[0])
    var toCurrency by mutableStateOf(Constants.currencyList[1])
    var currentRate by mutableDoubleStateOf(0.0)
    var errorMessage by mutableStateOf<String?>(null)
    var history = mutableStateListOf<String>()
    var calculatorVisibility by mutableStateOf(false)
    var rateUpdated = false


    fun parseCurrencyCode(currency: String) = currency.split(" - ")[0]

    fun parseCurrencyName(currency: String) = currency.split(" - ")[1]

    fun getRate(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val base = parseCurrencyCode(fromCurrency)
                val target = parseCurrencyCode(toCurrency)
                currentRate = repo.getExchangeRate(base, target)
                errorMessage = null
                rateUpdated = onComplete != null
                onComplete?.invoke()
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            }
        }
    }


    fun convert() {
        try {
            amount = fromAmount.toDouble()
            toAmount = "%.2f".format(amount * currentRate)
        } catch (e: NumberFormatException) {
            errorMessage = "Invalid amount"
        }
    }

    fun addToHistory() {
        if (amount != 0.0)
            history.add("$amount ${parseCurrencyName(fromCurrency)} = $toAmount ${parseCurrencyName(toCurrency)}")
    }

    fun swapCurrencies() {
        val temp = fromCurrency
        fromCurrency = toCurrency
        toCurrency = temp
        getRate()
    }
}