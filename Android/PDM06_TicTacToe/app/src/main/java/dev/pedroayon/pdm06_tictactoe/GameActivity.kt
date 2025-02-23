package dev.pedroayon.pdm06_tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.pedroayon.pdm06_tictactoe.components.TicTacToeGame

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val player1 = intent.getStringExtra("player1") ?: "Jugador 1"
        val player2 = intent.getStringExtra("player2") ?: "Jugador 2"

        setContent {
            TicTacToeGame(player1, player2)
        }
    }
}
