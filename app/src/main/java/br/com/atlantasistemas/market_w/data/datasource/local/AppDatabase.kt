package br.com.atlantasistemas.market_w.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.atlantasistemas.market_w.data.entities.Cart
import br.com.atlantasistemas.market_w.data.entities.ProdutoCarrinho
import br.com.atlantasistemas.market_w.data.entities.Usuario

@Database(entities = [Usuario::class, Cart::class, ProdutoCarrinho::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun daoDatabase() : DaoDatabase
}