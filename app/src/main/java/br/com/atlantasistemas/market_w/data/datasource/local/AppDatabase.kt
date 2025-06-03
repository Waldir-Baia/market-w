package br.com.atlantasistemas.market_w.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.atlantasistemas.market_w.data.entities.Usuario

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun daoDatabase() : DaoDatabase
}