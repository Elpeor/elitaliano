package com.ratatin.elitaliano.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ratatin.elitaliano.viewmodels.PagoViewModel
import com.ratatin.elitaliano.viewmodels.PagoViewModelFactory



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoView(
    userId: Int,
) {
    Log.d("test", "$userId")
    val context = LocalContext.current
    val pagoViewModel: PagoViewModel = viewModel(
        factory = PagoViewModelFactory(context)
    )


    LaunchedEffect(userId) {
        pagoViewModel.getPagosByIdUser(userId)
    }
    val pagos by pagoViewModel.pagoUserList.observeAsState(emptyList())
    Log.d("test", "$pagos")
    var total:Double = 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagos Realizados") },
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {


            pagos.groupBy { it.numTransaccion }.forEach { (numTrans, pagosGrupo) ->

                item {
                    Text(
                        text = "Transacción: $numTrans",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Fecha: ${pagosGrupo[0].fecha}", style = MaterialTheme.typography.bodyMedium)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            pagosGrupo.forEach { pago ->
                                total += pago.producto.precio * pago.cant
                                Text("${pago.producto.nombre} X ${pago.cant}", style = MaterialTheme.typography.bodyMedium)
                                Text("Subtotal: ${pago.producto.precio * pago.cant} CLP", style = MaterialTheme.typography.bodyMedium)
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                            Text("Total: $total")
                            total = 0.0
                        }
                    }
                }
            }
        }
    }
}