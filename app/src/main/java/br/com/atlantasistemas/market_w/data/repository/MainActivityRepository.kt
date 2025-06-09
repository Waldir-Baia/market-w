package br.com.atlantasistemas.market_w.data.repository

import br.com.atlantasistemas.market_w.data.datasource.local.DaoDatabase
import br.com.atlantasistemas.market_w.data.datasource.remote.ApiService
import br.com.atlantasistemas.market_w.data.entities.Cart
import br.com.atlantasistemas.market_w.data.entities.ProdutoCarrinho
import br.com.atlantasistemas.market_w.data.entities.models.response.ProdutoResponseJson
import br.com.atlantasistemas.market_w.data.entities.relation.CarrinhoComProduto
import retrofit2.Response
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    private val simpleApi: ApiService,
    private val daoDatabase: DaoDatabase
){

    //Local
    suspend fun criarCarrinho(): Long {
        return  daoDatabase.inserirCarrinho(Cart())
    }

    suspend fun getCarrinhoAtual(): CarrinhoComProduto? {
        return daoDatabase.obterCarrinhoAtual()
    }

    suspend fun adicionarProduto(produto: ProdutoCarrinho) {
        daoDatabase.inserirProdutos(produto)
    }

    //Remote
    suspend fun getProdutos(): Response<List<ProdutoResponseJson>>{
        return simpleApi.getProdutos()
    }

}