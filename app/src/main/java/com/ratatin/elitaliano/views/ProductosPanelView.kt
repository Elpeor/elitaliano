package com.ratatin.elitaliano.views

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
import com.ratatin.elitaliano.dataSQL.Producto
import com.ratatin.elitaliano.viewmodels.ProductosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosPanelView(
    productosViewModel: ProductosViewModel
) {


    val productos by productosViewModel.productoList.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var productoEditando by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Panel de productos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                productoEditando = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (productos.isEmpty()) {
                Text(
                    "No hay productos disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(productos) { producto ->
                        ProductoItem(
                            producto = producto,
                            onEditar = {
                                productoEditando = producto
                                showDialog = true
                            },
                            onEliminar = {
                                productosViewModel.viewModelScope.launch {
                                    try {
                                        productosViewModel.productoRepo.eliminarPorId(producto.idProducto)
                                        productosViewModel.fetchProductos()
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
        DialogAgregarEditarProducto(
            productoInicial = productoEditando,
            onDismiss = { showDialog = false },
            onGuardar = { producto ->
                productosViewModel.viewModelScope.launch {
                    try {
                        if (productoEditando != null) {
                            productoEditando?.idProducto?.let { oldId ->
                                productosViewModel.productoRepo.actualizar(
                                    nombre = producto.nombre,
                                    descripcion = producto.descripcion,
                                    precio = producto.precio,
                                    categoria = producto.categoria,
                                    imagen = producto.imagen,
                                    oldId
                                )
                                productosViewModel.fetchProductos()
                            }
                        }else {

                            productosViewModel.productoRepo.agregar(
                                nombre = producto.nombre,
                                descripcion = producto.descripcion,
                                precio = producto.precio,
                                categoria = producto.categoria,
                                imagen = producto.imagen
                            )
                            productosViewModel.fetchProductos()
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
fun ProductoItem(
    producto: Producto,
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
            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(producto.descripcion, style = MaterialTheme.typography.bodyMedium)
            Text("Precio: $${producto.precio}")
            Text("Categoría: ${producto.categoria}")

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
fun DialogAgregarEditarProducto(
    productoInicial: Producto?,
    onDismiss: () -> Unit,
    onGuardar: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(productoInicial?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(productoInicial?.descripcion ?: "") }
    var precio by remember { mutableStateOf(productoInicial?.precio?.toString() ?: "") }
    var categoria by remember { mutableStateOf(productoInicial?.categoria ?: "") }
    var imagen by remember { mutableStateOf(productoInicial?.imagen ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (productoInicial == null) "Agregar producto" else "Editar producto")
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
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("Imagen(Url)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val precioDouble = precio.toDoubleOrNull()
                if (nombre.isNotBlank() && descripcion.isNotBlank() && precioDouble != null && categoria.isNotBlank()) {
                    onGuardar(
                        productoInicial?.copy(
                            nombre = nombre.trim(),
                            descripcion = descripcion.trim(),
                            precio = precioDouble,
                            categoria = categoria.trim(),
                            imagen = imagen
                        ) ?: Producto(
                            nombre = nombre.trim(),
                            descripcion = descripcion.trim(),
                            precio = precioDouble,
                            categoria = categoria.trim(),
                            imagen = imagen
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