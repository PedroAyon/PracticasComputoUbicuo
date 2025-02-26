package dev.pedroayon.pdm05b_abpj.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.pedroayon.pdm05b_abpj.data.remote.NetworkModule
import dev.pedroayon.pdm05b_abpj.data.repository.CurrencyRepository
import dev.pedroayon.pdm05b_abpj.presentation.CurrencyViewModel

@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainScreenUI()
        }
    }
}


@Composable
fun MainScreenUI(
    viewModel: CurrencyViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CurrencyViewModel(
                    CurrencyRepository(NetworkModule.currencyApiService)
                ) as T
            }
        }
    )
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Left side: Currency Converter UI
        Box(modifier = Modifier.weight(6f)) {
            CurrencyConverterUI(viewModel = viewModel)
        }

        // Right side: Calculator UI (shown when toggled)
        if (viewModel.calculatorVisibility) {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
            ) {
                CalculatorUI()
            }
        }
    }

}