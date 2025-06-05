package br.com.atlantasistemas.market_w.data.repository

import br.com.atlantasistemas.market_w.data.datasource.local.DaoDatabase
import br.com.atlantasistemas.market_w.data.datasource.remote.ApiService
import br.com.atlantasistemas.market_w.data.entities.models.response.ProdutoResponseJson
import retrofit2.Response
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    private val simpleApi: ApiService,
    private val daoDatabase: DaoDatabase
){

    //Local


    //Remote
    suspend fun getProdutos(): Response<List<ProdutoResponseJson>>{
        return simpleApi.getProdutos()
    }


}