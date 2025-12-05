package com.ratatin.elitaliano.views

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ratatin.elitaliano.data.usuario.Usuario
import com.ratatin.elitaliano.viewmodels.PagoViewModel
import com.ratatin.elitaliano.viewmodels.PagoViewModelFactory
import com.ratatin.elitaliano.viewmodels.ProductosViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarroView(
    productosViewModel: ProductosViewModel,
    user: Usuario?
) {
    val context = LocalContext.current
    val pagoViewModel: PagoViewModel = viewModel(
        factory = PagoViewModelFactory(context)
    )
    val carrito by productosViewModel.carro.collectAsState()

    val productosAgrupados = carrito.groupBy { it.idProducto }

    var numTransaccion: Long

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Carrito de compras",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("El carrito está vacío")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                productosAgrupados.forEach { (_, listaProductos) ->
                    val producto = listaProductos.first()
                    val cantidad = listaProductos.size

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Precio unitario: $${producto.precio}")
                                Text("Subtotal: $${producto.precio * cantidad}")
                                Spacer(Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            productosViewModel.eliminarUnidadDelCarro(producto)
                                        }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                                        }

                                        Text("$cantidad", modifier = Modifier.padding(horizontal = 8.dp))

                                        IconButton(onClick = {
                                            productosViewModel.agregarAlCarro(producto)
                                        }) {
                                            Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                        }
                                    }

                                    IconButton(onClick = {
                                        repeat(cantidad) {
                                            productosViewModel.eliminarUnidadDelCarro(producto)
                                        }
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar producto")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total: $${productosViewModel.totalCarro()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = {
                    productosViewModel.viewModelScope.launch {
                        numTransaccion = pagoViewModel.generarNumTransaccion()
                        productosAgrupados.forEach { (_, listaProductos) ->
                            val producto = listaProductos.first()
                            val cantidad = listaProductos.size

                            pagoViewModel.pagoRepo.agregar(user, cantidad, producto, numTransaccion)
                        }
                    }
                    productosViewModel.limpiarCarro()
                    Toast.makeText(context, "Compra realizada", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Comprar")
                }
            }
        }
    }
}
