package br.com.atlantasistemas.market_w.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.atlantasistemas.market_w.utils.Utils.dadosProdutoGlobal
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.data.repository.MainActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetalhesProdutoViewModel @Inject constructor(
    private val repository: MainActivityRepository
) : ViewModel() {

    private val _dadosProdutoViewModel = MutableLiveData<Produtos>()
    val dadosProdutoViewModel: LiveData<Produtos> = _dadosProdutoViewModel

    init {
        pegarValores()
    }

    private fun pegarValores(){
        _dadosProdutoViewModel.value = dadosProdutoGlobal.value
    }


}