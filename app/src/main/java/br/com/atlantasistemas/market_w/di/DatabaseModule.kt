package br.com.atlantasistemas.market_w.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import br.com.atlantasistemas.market_w.data.datasource.local.AppDatabase
import br.com.atlantasistemas.market_w.data.datasource.local.DaoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatababse(@ApplicationContext appContext: Context) : AppDatabase{
        val database = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "atl_gestao_equipe"
        )
            .fallbackToDestructiveMigration()
            .build()

        Log.d("RoomDatabase", "Banco de dados criado com sucesso: $database")

        try {
            database.openHelper.writableDatabase
            Log.d("RoomDatabase", "Banco de dados aberto com sucesso")

        }catch (e: Exception){
            Log.e("RoomDatabase", "Erro ao abrir banco de dados: ${e.message}")
        }
        return database
    }

    @Provides
    @Singleton
    fun provideDao(database: AppDatabase): DaoDatabase{
        return database.daoDatabase()
    }
}