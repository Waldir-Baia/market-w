package br.com.atlantasistemas.market_w.data.datasource.remote

import br.com.atlantasistemas.market_w.data.entities.models.response.ProdutoResponseJson
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProdutos(): Response<List<ProdutoResponseJson>>

}