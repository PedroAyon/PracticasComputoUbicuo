package dev.pedroayon.pdm08_abpj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.pedroayon.pdm08_abpj.ui.theme.PDM08_ABPJTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PDM08_ABPJTheme {
                CatNamingScreen()
            }
        }
    }
}

@Composable
fun CatNamingScreen() {
    var catName by remember { mutableStateOf("") }
    var displayedName by remember { mutableStateOf("Name the cat") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = displayedName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        AsyncImage(
            model = "https://images.unsplash.com/photo-1529778873920-4da4926a72c2",
            contentDescription = "Cute cat photo",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 600.dp) // Set max height to 300dp
                .clip(RoundedCornerShape(8.dp))
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        OutlinedTextField(
            value = catName,
            onValueChange = { catName = it },
            label = { Text("Enter cat name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Pets,
                    contentDescription = "Cat icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { displayedName = catName; catName = "" }) {
                    Text("OK", color = MaterialTheme.colorScheme.primary)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

    }
}