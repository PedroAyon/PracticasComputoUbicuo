package dev.pedroayon.pdm12
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Modelo de datos para la tarea
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val dateTime: String, // Formateado como "yyyy-MM-dd HH:mm"
    val completed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListScreen() {
    // Lista mutable de tareas
    val tasks = remember { mutableStateListOf<Task>() }
    // Estado para controlar la visualización del diálogo (agregar/editar)
    var showDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Tareas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingTask = null  // Modo agregar
                showDialog = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onEdit = {
                        editingTask = it
                        showDialog = true
                    },
                    onDelete = { tasks.remove(it) },
                    onToggleComplete = { t ->
                        val index = tasks.indexOfFirst { it.id == t.id }
                        if (index != -1) {
                            tasks[index] = t.copy(completed = !t.completed)
                        }
                    }
                )
            }
        }
    }

    if (showDialog) {
        TaskDialog(
            task = editingTask,
            onDismiss = {
                showDialog = false
                editingTask = null
            },
            onConfirm = { title, description, dateTime ->
                if (editingTask != null) {
                    // Modo edición: actualizamos la tarea existente
                    val index = tasks.indexOfFirst { it.id == editingTask!!.id }
                    if (index != -1) {
                        tasks[index] = editingTask!!.copy(
                            title = title,
                            description = description,
                            dateTime = dateTime
                        )
                    }
                } else {
                    // Modo agregar: creamos una nueva tarea
                    val newId = if (tasks.isEmpty()) 0 else tasks.maxOf { it.id } + 1
                    tasks.add(Task(id = newId, title = title, description = description, dateTime = dateTime))
                }
                showDialog = false
                editingTask = null
            }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = "Fecha y hora: ${task.dateTime}",
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            IconButton(onClick = { onToggleComplete(task) }) {
                Text(if (task.completed) "✔️" else "⭕")
            }
            IconButton(onClick = { onEdit(task) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar tarea")
            }
            IconButton(onClick = { onDelete(task) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar tarea")
            }
        }
    }
}

@Composable
fun TaskDialog(
    task: Task?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    // Estados para título y descripción
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    // Usamos un Calendar para manejar fecha y hora
    val calendar = remember { Calendar.getInstance() }
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    var dateTimeString by remember { mutableStateOf(task?.dateTime ?: sdf.format(calendar.time)) }

    // Si se está editando y la tarea tiene fecha, se parsea el string y se actualiza el calendar
    LaunchedEffect(task) {
        if (task != null && task.dateTime.isNotEmpty()) {
            try {
                val date = sdf.parse(task.dateTime)
                if (date != null) {
                    calendar.time = date
                    dateTimeString = sdf.format(calendar.time)
                }
            } catch (e: Exception) {
                // En caso de error, se ignora
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (task == null) "Nueva Tarea" else "Editar Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Botón que lanza el selector de fecha y hora
                DateTimePickerButton(
                    calendar = calendar,
                    onDateTimeSelected = { updatedCalendar ->
                        dateTimeString = sdf.format(updatedCalendar.time)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Fecha y hora: $dateTimeString")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(title, description, dateTimeString) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DateTimePickerButton(
    calendar: Calendar,
    onDateTimeSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showDatePicker = false
                showTimePicker = true // Una vez seleccionada la fecha, se muestra el selector de hora
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                showTimePicker = false
                onDateTimeSelected(calendar)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Button(onClick = { showDatePicker = true }) {
        Text("Seleccionar Fecha y Hora")
    }
}
