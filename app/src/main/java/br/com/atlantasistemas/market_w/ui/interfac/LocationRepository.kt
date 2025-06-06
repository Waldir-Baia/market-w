package br.com.atlantasistemas.market_w.ui.interfac

import android.location.Location

interface LocationRepository {
    suspend fun getLocalizacao(): Location?
}