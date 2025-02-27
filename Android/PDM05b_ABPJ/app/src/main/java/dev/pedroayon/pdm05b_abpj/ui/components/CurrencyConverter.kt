package dev.pedroayon.pdm05b_abpj.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.pedroayon.pdm05b_abpj.presentation.CurrencyViewModel

@Composable
fun CurrencyConverterUI(viewModel: CurrencyViewModel) {
    var historyVisibility by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "Currency Converter",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Current Rate Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "1 ${viewModel.parseCurrencyCode(viewModel.fromCurrency)} = ${viewModel.currentRate} ${viewModel.parseCurrencyCode(viewModel.toCurrency)}",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.getRate() }) {
                        Text("Get Rate from API")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // "From" Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "From:", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = viewModel.fromAmount,
                            onValueChange = { viewModel.fromAmount = it },
                            label = { Text("From Amount") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CurrencyDropdown(
                            selected = viewModel.fromCurrency,
                            onSelectedChange = { viewModel.fromCurrency = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // "To" Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "To:", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = viewModel.toAmount,
                            onValueChange = { /* read-only field */ },
                            label = { Text("To Amount") },
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CurrencyDropdown(
                            selected = viewModel.toCurrency,
                            onSelectedChange = { viewModel.toCurrency = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(
                            onClick = {
                                viewModel.getRate { viewModel.convert() }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Convert")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { viewModel.swapCurrencies() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Swap")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { viewModel.addToHistory() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add To History")
                }
                Spacer(Modifier.width(4.dp))
                Button(onClick = { historyVisibility = !historyVisibility },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Toggle History")
                }
                Spacer(Modifier.width(4.dp))
                Button(onClick = { viewModel.calculatorVisibility = !viewModel.calculatorVisibility },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Calculator " + if (viewModel.calculatorVisibility) "<<" else ">>")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // History Section
            if (historyVisibility) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (viewModel.history.isNotEmpty()) {
                            viewModel.history.forEach { entry ->
                                Text(text = entry)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        } else {
                            Text(text = "Empty history")
                        }
                    }
                }
            }
        }
    }
}
