package br.com.atlantasistemas.market_w.utils

import androidx.lifecycle.MutableLiveData
import br.com.atlantasistemas.market_w.data.entities.Produtos
import java.text.NumberFormat
import java.util.Locale

object Utils {
    lateinit var dadosProdutoGlobal: MutableLiveData<Produtos>

    // Função para formatar o valor
    fun formatarParaRealBrasileiro(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }



}