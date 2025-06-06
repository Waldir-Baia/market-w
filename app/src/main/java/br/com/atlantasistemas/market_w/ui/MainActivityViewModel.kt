package br.com.atlantasistemas.market_w.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atlantasistemas.market_w.utils.Utils.dadosProdutoGlobal
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.data.repository.MainActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainActivityRepository
) : ViewModel() {

    private val _produtosPromocao = MutableLiveData<List<Produtos>>()
    val produtoPromocao: LiveData<List<Produtos>> = _produtosPromocao

    private val _dadosProduto = MutableLiveData<Produtos>()
    val dadosProduto: LiveData<Produtos> = _dadosProduto


    init {
        buscarDados()
    }

    private fun buscarDados(){
        viewModelScope.launch {
            importarProdutos()
        }
    }

    private suspend fun importarProdutos(){
        val response = repository.getProdutos()
        if(response.isSuccessful){
            response.body()?.let { result ->
                val listProduto = result.map { mapProduto ->
                    Produtos(
                        codigo = mapProduto.id,
                        nomeProduto = mapProduto.title,
                        descricao = mapProduto.description,
                        valor = mapProduto.price,
                        imageProduto = mapProduto.image
                    )
                }
                _produtosPromocao.value = listProduto
            }
        }
    }

    fun pegarDadosProduto(){
        _dadosProduto.value = dadosProdutoGlobal.value
    }

}