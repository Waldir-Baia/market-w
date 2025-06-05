package br.com.atlantasistemas.market_w.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        buscarDados()
    }

    private fun buscarDados(){
//        // Criando produtos fictícios
//        val listaProdutos = listOf(
//            Produtos(1, "Cenoura Fresca", "Cenoura orgânica, direto da fazenda", 4.99),
//            Produtos(2, "Tomate Italiano", "Tomate maduro e suculento", 6.49),
//            Produtos(3, "Alface Crespa", "Alface fresca para saladas", 3.25),
//            Produtos(4, "Batata Doce", "Batata doce rica em fibras", 5.99),
//            Produtos(5, "Maçã Fuji", "Maçã crocante e doce", 7.50)
//        )
//        _produtosPromocao.value = listaProdutos


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

}