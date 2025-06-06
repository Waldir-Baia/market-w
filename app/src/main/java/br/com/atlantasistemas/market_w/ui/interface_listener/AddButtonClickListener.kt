package br.com.atlantasistemas.market_w.ui.interface_listener

import br.com.atlantasistemas.market_w.data.entities.Produtos

interface AddButtonClickListener {
    fun onAddButtonClick(produto: Produtos)
}