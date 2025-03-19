package dev.pedroayon.pdm10_abpj

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.pedroayon.pdm10_abpj.presentation.PersonViewModel
import dev.pedroayon.pdm10_abpj.ui.theme.PDM10_ABPJTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PDM10_ABPJTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PersonForm()
                }
            }
        }
    }
}

@Composable
fun PersonForm(viewModel: PersonViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Masculino") }
    var selectedDegree by remember { mutableStateOf("Bachillerato") }
    var isNational by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Estados para mensajes de error
    var nameError by remember { mutableStateOf<String?>(null) }
    var ageError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                if(it.trim().isNotEmpty()) nameError = null
            },
            label = { Text("Nombre") },
            isError = nameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError != null) {
            Text(
                text = nameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        OutlinedTextField(
            value = ageText,
            onValueChange = {
                ageText = it
                // Se elimina el error si se ingresa un valor válido
                if(it.toIntOrNull() != null && it.toInt() > 0) ageError = null
            },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = ageError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (ageError != null) {
            Text(
                text = ageError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Text("Sexo")
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedGender == "Masculino",
                onClick = { selectedGender = "Masculino" }
            )
            Text("Masculino", modifier = Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedGender == "Femenino",
                onClick = { selectedGender = "Femenino" }
            )
            Text("Femenino", modifier = Modifier.padding(start = 4.dp))
        }
        DegreeDropdown(selectedDegree = selectedDegree, onDegreeSelected = { selectedDegree = it })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isNational, onCheckedChange = { isNational = it })
            Text("Nacional")
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // Validaciones antes de agregar la persona
                    var isValid = true
                    if (name.trim().isEmpty()) {
                        nameError = "El nombre es obligatorio"
                        isValid = false
                    }
                    val age = ageText.toIntOrNull() ?: 0
                    if (age <= 0) {
                        ageError = "Edad incorrecta"
                        isValid = false
                    }
                    if (isValid) {
                        viewModel.addPerson(name, age, selectedGender, selectedDegree, isNational)
                        name = ""
                        ageText = ""
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Agregar")
            }
            Button(
                onClick = {
                    context.startActivity(Intent(context, StatsActivity::class.java))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Estadístico")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DegreeDropdown(
    selectedDegree: String,
    onDegreeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val degrees = listOf("Bachillerato", "Licenciatura", "Maestría", "Doctorado")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedDegree,
            onValueChange = { },
            label = { Text("Grado Académico") },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            degrees.forEach { degree ->
                DropdownMenuItem(
                    text = { Text(degree) },
                    onClick = {
                        onDegreeSelected(degree)
                        expanded = false
                    }
                )
            }
        }
    }
}
