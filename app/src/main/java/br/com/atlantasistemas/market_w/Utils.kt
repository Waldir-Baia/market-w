package br.com.atlantasistemas.market_w

import java.text.NumberFormat
import java.util.Locale

object Utils {
    // Função para formatar o valor
    fun formatarParaRealBrasileiro(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }



}