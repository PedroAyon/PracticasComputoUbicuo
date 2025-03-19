package dev.pedroayon.pdm14

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PixelArtDrawerScreen() {
    var gridSizeInput by remember { mutableStateOf("16") }
    var gridSize by remember { mutableStateOf(16) }

    var selectedColor by remember { mutableStateOf(Color.Black) }

    val availableColors = listOf(
        Color.Black, Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan, Color.Gray
    )

    val gridColors = remember(gridSize) {
        mutableStateListOf<Color>().apply {
            repeat(gridSize * gridSize) {
                add(Color.White)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = gridSizeInput,
                onValueChange = { gridSizeInput = it },
                label = { Text("Tamaño (N)") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                gridSizeInput.toIntOrNull()?.let { newSize ->
                    gridSize = newSize
                }
            }) {
                Text("Actualizar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color)
                        .border(
                            width = if (selectedColor == color) 3.dp else 1.dp,
                            color = if (selectedColor == color) Color.Black else Color.LightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(gridColors.size) { index ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(gridColors[index])
                        .clickable {
                            gridColors[index] = selectedColor
                        }
                )
            }
        }
    }
}
