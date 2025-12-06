package com.ratatin.elitaliano.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratatin.elitaliano.dataSQL.Pago
import com.ratatin.elitaliano.repository.PagoRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PagoViewModel(val pagoRepo: PagoRepository) : ViewModel() {


    private val _pagoList = MutableLiveData<List<Pago>>(emptyList())
    val pagoList : LiveData<List<Pago>> = _pagoList

    private val _pagoUserList = MutableLiveData<List<Pago>>(emptyList())
    val pagoUserList : LiveData<List<Pago>> = _pagoUserList
    @RequiresApi(Build.VERSION_CODES.O)
    fun generarNumTransaccion(): Long {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        Log.d("numTransaccion", now.format(formatter).toString())
        return now.format(formatter).toLong()
    }


    init {
        fetchPagos()
        viewModelScope.launch {

        }
    }


    fun fetchPagos() {
        viewModelScope.launch {
            try {
                _pagoList.value = pagoRepo.getPagos()
            } catch (e: Exception){
                println("error al obtener datos: ${e.localizedMessage}")
            }

        }
    }

    fun getPagosByIdUser(idUsuario: Long?) {
        viewModelScope.launch {
            _pagoUserList.value = pagoRepo.getPagosByIdUsuario(idUsuario)
            Log.d("test", "en pagoviewmodel: ${_pagoUserList.value}")
        }
    }
}