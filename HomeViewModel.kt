package com.vestbem.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vestbem.app.data.FirebaseService
import com.vestbem.app.data.Produto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {
    
    private val _produtos = MutableLiveData<List<Produto>>()
    val produtos: LiveData<List<Produto>> = _produtos
    
    private var produtosCache = emptyList<Produto>()
    
    init {
        carregarProdutos()
    }
    
    private fun carregarProdutos() {
        viewModelScope.launch {
            // Simula stream em tempo real do Firestore
            firebaseService.getProdutosStream().addSnapshotListener { snapshot, _ ->
                snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Produto::class.java)?.copy(id = doc.id)
                }?.let { produtos ->
                    produtosCache = produtos
                    _produtos.value = produtos
                }
            }
        }
    }
    
    fun filtrarPorCategoria(categoria: String) {
        val filtrados = if (categoria == "todas") {
            produtosCache
        } else {
            produtosCache.filter { it.categoria.lowercase() == categoria }
        }
        _produtos.value = filtrados
    }
}
