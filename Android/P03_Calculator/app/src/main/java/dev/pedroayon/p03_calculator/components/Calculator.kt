package dev.pedroayon.p03_calculator.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.pedroayon.p03_calculator.ui.theme.P03_CalculatorTheme

@Composable
fun CalculatorApp() {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Simple Calculator", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Enter first number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = number2,
            onValueChange = { number2 = it },
            label = { Text("Enter second number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OperationButton(symbol = "+") {
                result = calculate(number1, number2, Operation.Add)
            }
            OperationButton(symbol = "-") {
                result = calculate(number1, number2, Operation.Subtract)
            }
            OperationButton(symbol = "×") {
                result = calculate(number1, number2, Operation.Multiply)
            }
            OperationButton(symbol = "÷") {
                result = calculate(number1, number2, Operation.Divide)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Result: $result",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun OperationButton(symbol: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.size(64.dp)) {
        Text(text = symbol, style = MaterialTheme.typography.headlineSmall)
    }
}

enum class Operation {
    Add, Subtract, Multiply, Divide
}

fun calculate(num1: String, num2: String, operation: Operation): String {
    val number1 = num1.toDoubleOrNull()
    val number2 = num2.toDoubleOrNull()

    if (number1 == null || number2 == null) {
        return "Invalid input"
    }

    return when (operation) {
        Operation.Add -> (number1 + number2).toString()
        Operation.Subtract -> (number1 - number2).toString()
        Operation.Multiply -> (number1 * number2).toString()
        Operation.Divide -> {
            if (number2 == 0.0) {
                "Error: Division by zero"
            } else {
                (number1 / number2).toString()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    P03_CalculatorTheme {
        CalculatorApp()
    }
}
