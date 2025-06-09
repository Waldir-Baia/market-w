package br.com.atlantasistemas.market_w.data.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import br.com.atlantasistemas.market_w.data.entities.Cart
import br.com.atlantasistemas.market_w.data.entities.ProdutoCarrinho

data class CarrinhoComProduto(
    @Embedded val carrinho: Cart,
    @Relation(
        parentColumn = "carrinhoId",
        entityColumn = "carrinhoId"
    )
    val produtos: List<ProdutoCarrinho>
)
