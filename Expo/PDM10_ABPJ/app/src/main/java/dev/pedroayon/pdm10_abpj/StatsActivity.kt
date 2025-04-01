package dev.pedroayon.pdm10_abpj

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.pedroayon.pdm10_abpj.presentation.PersonViewModel
import dev.pedroayon.pdm10_abpj.domain.model.Person

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
    val context = LocalContext.current
    var personToEdit by remember { mutableStateOf<Person?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = {
                context.startActivity(Intent(context, MainActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text(text = "Regresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estadísticas de Personas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(persons) { person ->
                PersonItem(
                    person = person,
                    onEdit = {
                        personToEdit = person
                        showDialog = true
                    },
                    onDelete = {
                        viewModel.removePerson(person)
                    }
                )
            }
        }
    }

    if (showDialog && personToEdit != null) {
        EditPersonDialog(
            person = personToEdit!!,
            onDismiss = { showDialog = false },
            onUpdate = { updatedPerson ->
                viewModel.updatePerson(updatedPerson)
                showDialog = false
            }
        )
    }
}


@Composable
fun PersonItem(person: Person, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Nombre: ${person.name}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Edad: ${person.age}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Género: ${person.gender}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Grado: ${person.degree}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Nacionalidad: ${if (person.isNational) "Nacional" else "Extranjero"}", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                Text("Editar", modifier = Modifier.clickable { onEdit() }, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Eliminar", modifier = Modifier.clickable { onDelete() }, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
fun EditPersonDialog(
    person: Person,
    onDismiss: () -> Unit,
    onUpdate: (Person) -> Unit
) {
    var name by remember { mutableStateOf(person.name) }
    var age by remember { mutableStateOf(person.age.toString()) }
    var gender by remember { mutableStateOf(person.gender) }
    var degree by remember { mutableStateOf(person.degree) }
    var isNational by remember { mutableStateOf(person.isNational) }

    val genderOptions = listOf("Masculino", "Femenino")
    val degreeOptions = listOf("Bachillerato", "Licenciatura", "Maestría", "Doctorado")

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Editar Persona") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                TextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),

                )

                Text(text = "Género", modifier = Modifier.padding(vertical = 4.dp))
                genderOptions.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == option,
                            onClick = { gender = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(option)
                    }
                }

                Text(text = "Grado", modifier = Modifier.padding(vertical = 4.dp))
                degreeOptions.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = degree == option,
                            onClick = { degree = option }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(option)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Es nacional?")
                    Checkbox(
                        checked = isNational,
                        onCheckedChange = { isNational = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && age.isNotBlank()) {
                        val updatedPerson = person.copy(
                            name = name,
                            age = age.toInt(),
                            gender = gender,
                            degree = degree,
                            isNational = isNational
                        )
                        onUpdate(updatedPerson)
                    }
                }
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
