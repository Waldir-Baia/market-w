package br.com.atlantasistemas.market_w.data.entities.models.response

data class ProdutoResponseJson(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
)