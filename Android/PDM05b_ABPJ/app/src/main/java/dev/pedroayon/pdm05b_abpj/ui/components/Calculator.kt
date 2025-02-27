package dev.pedroayon.pdm05b_abpj.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.pedroayon.pdm05b_abpj.util.evaluateExpression

@Composable
fun CalculatorUI() {
    var calculatorDisplay by remember { mutableStateOf("0") }

    fun appendDigit(digit: String) {
        calculatorDisplay = when (calculatorDisplay) {
            "0", "Error" -> if (digit == ".") calculatorDisplay + digit else digit
            else -> calculatorDisplay + digit
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display Section
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = calculatorDisplay,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Keypad Section
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KeypadButton("7", onClick = { appendDigit("7") }, modifier = Modifier.weight(1f))
                    KeypadButton("8", onClick = { appendDigit("8") }, modifier = Modifier.weight(1f))
                    KeypadButton("9", onClick = { appendDigit("9") }, modifier = Modifier.weight(1f))
                    KeypadButton("(", onClick = { appendDigit("(") }, modifier = Modifier.weight(1f))
                    KeypadButton(")", onClick = { appendDigit(")") }, modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KeypadButton("4", onClick = { appendDigit("4") }, modifier = Modifier.weight(1f))
                    KeypadButton("5", onClick = { appendDigit("5") }, modifier = Modifier.weight(1f))
                    KeypadButton("6", onClick = { appendDigit("6") }, modifier = Modifier.weight(1f))
                    KeypadButton("+", onClick = { appendDigit("+") }, modifier = Modifier.weight(1f))
                    KeypadButton("-", onClick = { appendDigit("-") }, modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KeypadButton("1", onClick = { appendDigit("1") }, modifier = Modifier.weight(1f))
                    KeypadButton("2", onClick = { appendDigit("2") }, modifier = Modifier.weight(1f))
                    KeypadButton("3", onClick = { appendDigit("3") }, modifier = Modifier.weight(1f))
                    KeypadButton("x", onClick = { appendDigit("*") }, modifier = Modifier.weight(1f))
                    KeypadButton("/", onClick = { appendDigit("/") }, modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KeypadButton("0", onClick = { appendDigit("0") }, modifier = Modifier.weight(1f))
                    KeypadButton(".", onClick = { appendDigit(".") }, modifier = Modifier.weight(1f))
                    KeypadButton("%", onClick = { appendDigit("%") }, modifier = Modifier.weight(1f))
                    KeypadButton("^", onClick = { appendDigit("^") }, modifier = Modifier.weight(1f))
                    KeypadButton("=", onClick = { calculatorDisplay = evaluateExpression(calculatorDisplay) }, modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.weight(3f))
                    KeypadButton("C", onClick = { calculatorDisplay = "0" }, modifier = Modifier.weight(1f))
                    KeypadButton("CE", onClick = {
                        calculatorDisplay = if(calculatorDisplay.length > 1) calculatorDisplay.dropLast(1) else "0"
                    }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
