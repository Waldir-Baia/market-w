package br.com.atlantasistemas.market_w.data.entities

data class Produtos(
    val codigo: Int,
    val nomeProduto: String,
    val descricao: String,
    val valor: Double,
    val imageProduto: String,
    val quantidade: Int
)
