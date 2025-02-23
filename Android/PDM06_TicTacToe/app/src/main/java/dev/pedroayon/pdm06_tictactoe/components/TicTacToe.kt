package dev.pedroayon.pdm06_tictactoe.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TicTacToeGame(player1: String, player2: String) {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }

    fun resetBoard() {
        board = List(9) { "" }
        winner = null
        currentPlayer = "X"
    }

    fun checkWinner(board: List<String>): String? {
        val winningPositions = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (pos in winningPositions) {
            if (board[pos[0]] != "" && board[pos[0]] == board[pos[1]] && board[pos[1]] == board[pos[2]]) {
                return board[pos[0]]
            }
        }
        return null
    }

    fun handleCellClick(index: Int) {
        if (winner != null || board[index].isNotEmpty()) return
        board = board.toMutableList().also { it[index] = currentPlayer }
        checkWinner(board)?.let { winner = it; return }
        if (board.none { it.isEmpty() }) { winner = "Empate"; return }
        currentPlayer = if (currentPlayer == "X") "O" else "X"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "¡A Jugar!", style = MaterialTheme.typography.titleLarge)

        if (winner == null) {
            val currentPlayerName = if (currentPlayer == "X") player1 else player2
            Text(text = "Turno de $currentPlayerName ($currentPlayer)", style = MaterialTheme.typography.titleMedium)
        } else {
            val winningName = when (winner) {
                "X" -> player1
                "O" -> player2
                "Empate" -> "¡Empate!"
                else -> ""
            }
            Text(text = if (winner == "Empate") winningName else "Ganó $winningName ($winner)", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    val index = row * 3 + col
                    val cellValue = board[index]
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .background(if (cellValue.isEmpty()) Color.LightGray else Color.Gray, shape = RoundedCornerShape(8.dp))
                            .clickable { handleCellClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = cellValue, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { resetBoard() }) {
            Text(text = "Reiniciar Juego")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicTacToeGamePreview() {
    TicTacToeGame("Jugador 1", "Jugador 2")
}
