package com.ratatin.elitaliano.views

import ads_mobile_sdk.ad
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ratatin.elitaliano.dataSQL.Usuario
import com.ratatin.elitaliano.viewmodels.LoginViewModel
import com.ratatin.elitaliano.viewmodels.LoginViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosPanelView(

) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory()
    )

    val usuarios by viewModel.usuarioList.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var usuarioEditando by remember { mutableStateOf<Usuario?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Panel de usuarioss") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                usuarioEditando = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar usuario")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (usuarios.isEmpty()) {
                Text(
                    "No hay usuarios disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(usuarios) { usuario ->
                        UsuarioItem(
                            usuario = usuario,
                            onEditar = {
                                usuarioEditando = usuario
                                showDialog = true
                            },
                            onEliminar = {
                                viewModel.viewModelScope.launch {
                                    try {
                                        viewModel.usuarioRepo.eliminarPorId(usuario.idUsuario)
                                        viewModel.fetchUsuarios()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        DialogAgregarEditarUsuario(
            usuarioInicial = usuarioEditando,
            onDismiss = { showDialog = false },
            onGuardar = { usuario ->
                viewModel.viewModelScope.launch {
                    try {
                        if (usuarioEditando != null) {
                            usuarioEditando?.idUsuario?.let { oldId ->
                                viewModel.usuarioRepo.actualizar(
                                    nombre = usuario.nombre,
                                    email = usuario.email,
                                    password = usuario.password,
                                    admin = usuario.admin,
                                    oldId
                                )
                                viewModel.fetchUsuarios()
                            }
                        }else {
                            if(!viewModel.emailInList(usuario.email)){
                            viewModel.usuarioRepo.createUsuario(
                                nombre = usuario.nombre,
                                email = usuario.email,
                                password = usuario.password,
                                admin = usuario.admin
                            )
                            viewModel.fetchUsuarios()
                        }else{
                            println("Ya existe un usuario con ese email")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun UsuarioItem(
    usuario: Usuario,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(usuario.nombre, style = MaterialTheme.typography.titleMedium)
            Text(usuario.email, style = MaterialTheme.typography.bodyMedium)
            Text("Contraseña: $${usuario.password}")
            Text("admin?: ${usuario.admin}")

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Composable
fun DialogAgregarEditarUsuario(
    usuarioInicial: Usuario?,
    onDismiss: () -> Unit,
    onGuardar: (Usuario) -> Unit
) {
    var nombre by remember { mutableStateOf(usuarioInicial?.nombre ?: "") }
    var email by remember { mutableStateOf(usuarioInicial?.email ?: "") }
    var password by remember { mutableStateOf(usuarioInicial?.password ?: "") }
    var admin by remember { mutableStateOf(usuarioInicial?.admin ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (usuarioInicial == null) "Agregar usuario" else "Editar usuario")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Checkbox(
                        checked = admin,
                        onCheckedChange = { admin = it }
                    )
                    Text("Administrador")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    onGuardar(
                        usuarioInicial?.copy(
                            nombre = nombre.trim(),
                            email = email.trim(),
                            password = password,
                            admin = admin
                        ) ?: Usuario(
                            nombre = nombre.trim(),
                            email = email.trim(),
                            password = password,
                            admin = admin
                        )
                    )
                }
            }) {
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