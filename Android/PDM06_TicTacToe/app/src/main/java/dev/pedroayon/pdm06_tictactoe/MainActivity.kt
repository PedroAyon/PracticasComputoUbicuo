package dev.pedroayon.pdm06_tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen { player1, player2 ->
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra("player1", player1)
                    putExtra("player2", player2)
                }
                startActivity(intent)
            }
        }
    }
}

@Composable
fun MainScreen(onStartGame: (String, String) -> Unit) {
    var player1 by remember { mutableStateOf("") }
    var player2 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Introduce Nombres", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = player1,
            onValueChange = { player1 = it },
            label = { Text("Jugador 1") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = player2,
            onValueChange = { player2 = it },
            label = { Text("Jugador 2") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onStartGame(player1, player2) },
            enabled = player1.isNotBlank() && player2.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Comenzar Juego")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen { _, _ -> }
}
