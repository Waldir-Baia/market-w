package br.com.atlantasistemas.market_w.data.repository

import br.com.atlantasistemas.market_w.data.datasource.local.DaoDatabase
import br.com.atlantasistemas.market_w.data.datasource.remote.ApiService
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    private val simpleApi: ApiService,
    private val daoDatabase: DaoDatabase
){
}