package br.com.atlantasistemas.market_w.ui.diff

import androidx.recyclerview.widget.DiffUtil
import br.com.atlantasistemas.market_w.data.entities.Produtos

object ProdutoDiffCallback : DiffUtil.ItemCallback<Produtos>() {
    override fun areItemsTheSame(
        oldItem: Produtos,
        newItem: Produtos
    ): Boolean {
        return oldItem.codigo == newItem.codigo
    }

    override fun areContentsTheSame(
        oldItem: Produtos,
        newItem: Produtos
    ): Boolean {
        return  oldItem == newItem
    }
}