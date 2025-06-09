package br.com.atlantasistemas.market_w.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atlantasistemas.market_w.data.entities.ProdutoCarrinho
import br.com.atlantasistemas.market_w.utils.Utils.dadosProdutoGlobal
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.data.entities.relation.CarrinhoComProduto
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

    private val _carrinho = MutableLiveData<CarrinhoComProduto?>()
    val carrinho: LiveData<CarrinhoComProduto?> = _carrinho


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
                        imageProduto = mapProduto.image,
                        quantidade = 0
                    )
                }
                _produtosPromocao.value = listProduto
            }
        }
    }

    fun pegarDadosProduto(){
        _dadosProduto.value = dadosProdutoGlobal.value
    }

    fun incrementar(produto: Produtos) {
        val atual = _produtosPromocao.value?.toMutableList() ?: return
        val index = atual.indexOfFirst { it.codigo == produto.codigo }
        if (index != -1) {
            val atualProduto = atual[index]
            atual[index] = atualProduto.copy(quantidade = atualProduto.quantidade + 1)
            _produtosPromocao.value = atual
        }
    }

    fun carregarCarrinho(){
        viewModelScope.launch {
            val carrinhoAtual = repository.getCarrinhoAtual()
            if(carrinhoAtual != null){
                _carrinho.postValue(carrinhoAtual)
            }else{
                val novoId = repository.criarCarrinho()
                val novoCarrinho = repository.getCarrinhoAtual()
                _carrinho.postValue(novoCarrinho)
            }
        }
    }


    fun adicionarProdutoAoCarrinho(produto: ProdutoCarrinho) {
        viewModelScope.launch {
            repository.adicionarProduto(produto)
            carregarCarrinho()
        }
    }


}