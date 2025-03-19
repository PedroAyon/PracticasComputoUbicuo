package dev.pedroayon.pdm10_abpj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.pedroayon.pdm10_abpj.presentation.PersonViewModel

class StatsActivity : ComponentActivity() {
    private val viewModel: PersonViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StatsScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun StatsScreen(viewModel: PersonViewModel) {
    val persons by viewModel.persons.collectAsState()

    val total = persons.size
    val averageAge = if (total > 0) persons.sumOf { it.age } / total.toFloat() else 0f
    val countByDegree = persons.groupingBy { it.degree }.eachCount()
    val youngest = persons.minByOrNull { it.age }
    val oldest = persons.maxByOrNull { it.age }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total de individuos:", style = MaterialTheme.typography.bodyLarge)
                    Text("$total", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Promedio de edad:", style = MaterialTheme.typography.bodyLarge)
                    Text("%.2f".format(averageAge), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cantidad por grado académico:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                countByDegree.forEach { (degree, count) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(degree, style = MaterialTheme.typography.bodyMedium)
                        Text("$count", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                youngest?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("El más joven:", style = MaterialTheme.typography.bodyLarge)
                        Text("${it.name} (${it.age} años)", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                oldest?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("El más viejo:", style = MaterialTheme.typography.bodyLarge)
                        Text("${it.name} (${it.age} años)", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}