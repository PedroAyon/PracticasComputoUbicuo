package dev.pedroayon.pdm13

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// Modelo de datos para el usuario
data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen() {
    // Lista mutable de usuarios
    val users = remember { mutableStateListOf<User>() }
    // Estados para controlar el diálogo de agregar/editar
    var showDialog by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Usuarios") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingUser = null  // Modo agregar
                showDialog = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar usuario")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(users, key = { it.id }) { user ->
                UserItem(
                    user = user,
                    onEdit = {
                        editingUser = it
                        showDialog = true
                    },
                    onDelete = { users.remove(it) }
                )
            }
        }
    }

    if (showDialog) {
        UserDialog(
            user = editingUser,
            onDismiss = {
                showDialog = false
                editingUser = null
            },
            onConfirm = { name, age, imageUrl ->
                if (editingUser != null) {
                    // Modo edición: actualizamos el usuario existente
                    val index = users.indexOfFirst { it.id == editingUser!!.id }
                    if (index != -1) {
                        users[index] = editingUser!!.copy(
                            name = name,
                            age = age,
                            imageUrl = imageUrl
                        )
                    }
                } else {
                    // Modo agregar: creamos un nuevo usuario
                    val newId = if (users.isEmpty()) 0 else users.maxOf { it.id } + 1
                    users.add(User(id = newId, name = name, age = age, imageUrl = imageUrl))
                }
                showDialog = false
                editingUser = null
            }
        )
    }
}

@Composable
fun UserItem(
    user: User,
    onEdit: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de usuario (avatar) cargada con Coil y recortada en forma circular
            AsyncImage(
                model = user.imageUrl,
                contentDescription = "Avatar de ${user.name}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Edad: ${user.age}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onEdit(user) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar usuario")
            }
            IconButton(onClick = { onDelete(user) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar usuario")
            }
        }
    }
}

@Composable
fun UserDialog(
    user: User?,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String) -> Unit
) {
    // Estados para cada campo del formulario
    var name by remember { mutableStateOf(user?.name ?: "") }
    var ageText by remember { mutableStateOf(user?.age?.toString() ?: "") }
    var imageUrl by remember { mutableStateOf(user?.imageUrl ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (user == null) "Nuevo Usuario" else "Editar Usuario") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL de Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Se convierte la edad a entero; si falla se toma 0 por defecto
                    val age = ageText.toIntOrNull() ?: 0
                    onConfirm(name, age, imageUrl)
                }
            ) {
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
