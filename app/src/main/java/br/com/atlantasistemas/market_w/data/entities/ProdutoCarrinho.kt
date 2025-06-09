package br.com.atlantasistemas.market_w.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "produto_carrinho",
    foreignKeys = [ForeignKey(
        entity = Cart::class,
        parentColumns = ["carrinhoId"],
        childColumns = ["carrinhoId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProdutoCarrinho(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val carrinhoId: Int,
    val produtoId: Int,
    val nomeProduto: String,
    val preco: Double,
    val quantidade: Int

)
