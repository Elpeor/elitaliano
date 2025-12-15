@file:JvmName("ProductosViewKt")

package com.ratatin.elitaliano.views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ratatin.elitaliano.viewmodels.ProductosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosView(
    navController: NavHostController,
    productosViewModel: ProductosViewModel
) {
    val context = LocalContext.current
    val productos by productosViewModel.productoList.observeAsState(emptyList())

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }
    var expanded by remember { mutableStateOf(false) }

    val categorias = remember(productos) {
        listOf("Todas") + productos.map { it.categoria }.distinct()
    }

    val productosFiltrados = productos.filter { producto ->
        val matchSearch =
            producto.nombre.contains(searchQuery, ignoreCase = true) ||
                    producto.descripcion.contains(searchQuery, ignoreCase = true)

        val matchCategory =
            selectedCategory == "Todas" || producto.categoria == selectedCategory

        matchSearch && matchCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos disponibles") },
                actions = {
                    IconButton(onClick = { navController.navigate("carro") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Ver carrito")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar producto") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                selectedCategory = categoria
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(productosFiltrados) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = producto.imagen,
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    producto.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(producto.descripcion)
                                Text(
                                    "Precio: $${producto.precio}",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Categoría: ${producto.categoria}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        productosViewModel.agregarAlCarro(producto)
                                        Toast.makeText(
                                            context,
                                            "${producto.nombre} agregado al carrito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Agregar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}