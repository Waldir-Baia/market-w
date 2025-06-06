package br.com.atlantasistemas.market_w.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.atlantasistemas.market_w.utils.Utils.dadosProdutoGlobal
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.data.repository.MainActivityRepository
import br.com.atlantasistemas.market_w.utils.Utils.formatarParaRealBrasileiro
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetalhesProdutoViewModel @Inject constructor(
    private val repository: MainActivityRepository
) : ViewModel() {

    private val _dadosProdutoViewModel = MutableLiveData<Produtos>()
    val dadosProdutoViewModel: LiveData<Produtos> = _dadosProdutoViewModel

    private val _quantidade = MutableLiveData(1)
    val quantidade: LiveData<Int> = _quantidade

    val precoTotal: LiveData<Double> = MediatorLiveData<Double>().apply {
        fun atualizar(){
            val produto = _dadosProdutoViewModel.value
            val qtde = _quantidade.value ?: 1
            val precoUnitario = produto?.valor?.toDouble() ?: 0.0
            value = precoUnitario * qtde
        }

        addSource(_dadosProdutoViewModel) { atualizar() }
        addSource(_quantidade) { atualizar() }
    }

    init {
        pegarValores()
    }

    private fun pegarValores(){
        _dadosProdutoViewModel.value = dadosProdutoGlobal.value
    }

    fun aumentarQuantidade() {
        _quantidade.value = (_quantidade.value ?: 1) + 1
    }

    fun diminuirQuantidade() {
        val atual = _quantidade.value ?: 1
        if (atual > 1) _quantidade.value = atual - 1
    }

}