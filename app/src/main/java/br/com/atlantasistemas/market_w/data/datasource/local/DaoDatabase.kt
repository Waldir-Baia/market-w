package br.com.atlantasistemas.market_w.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.atlantasistemas.market_w.data.entities.Cart
import br.com.atlantasistemas.market_w.data.entities.ProdutoCarrinho
import br.com.atlantasistemas.market_w.data.entities.relation.CarrinhoComProduto

@Dao
interface DaoDatabase {

    @Insert
    suspend fun inserirCarrinho(cart: Cart): Long

    @Insert
    suspend fun inserirProdutos(produtos: ProdutoCarrinho)

    @Transaction
    @Query("SELECT * FROM carrinho WHERE finalizado = 0 LIMIT 1")
    suspend fun obterCarrinhoAtual(): CarrinhoComProduto?


    @Query("UPDATE carrinho SET finalizado = 1 WHERE carrinhoId = :id")
    suspend fun finalizarCarrinho(id: Int)

}