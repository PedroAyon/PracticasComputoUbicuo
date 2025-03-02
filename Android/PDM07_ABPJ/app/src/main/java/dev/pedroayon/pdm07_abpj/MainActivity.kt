package dev.pedroayon.pdm07_abpj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.pedroayon.pdm07_abpj.ui.theme.PDM07_ABPJTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PDM07_ABPJTheme {
                ButtonsShowcaseScreen()
            }
        }
    }
}

@Composable
fun ButtonsShowcaseScreen() {
    var simpleClicked by remember { mutableStateOf(false) }
    var toggleOn by remember { mutableStateOf(false) }
    var switchOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón simple
        Button(
            onClick = { simpleClicked = !simpleClicked },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (simpleClicked) "¡Botón Simple Pulsado!" else "Click")
        }

        Button(
            onClick = { toggleOn = !toggleOn },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (toggleOn) Color(0xFF4CAF50) else Color(0xFFBDBDBD)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (toggleOn) "Toggle: ON" else "Toggle: OFF")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = switchOn,
                onCheckedChange = {
                    switchOn = it
                },
                )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (switchOn) "Switch: ON" else "Switch: OFF")
        }

        IconButton(
            onClick = {},
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.LightGray,
                contentColor = MaterialTheme.colorScheme.primary
            )        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Icono de estrella",
                modifier = Modifier.size(32.dp)
            )
        }

        Button(
            onClick = { /* Acción para Button+Image */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Icono de estrella",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Click con icono")
        }

        TextButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Botón sin borde")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsShowcaseScreenPreview() {
    ButtonsShowcaseScreen()
}
