package br.com.atlantasistemas.market_w.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrinho")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val carrinhoId: Int = 0,
    val dataCriacao: Long = System.currentTimeMillis(),
    val finalizado: Boolean = false

)
