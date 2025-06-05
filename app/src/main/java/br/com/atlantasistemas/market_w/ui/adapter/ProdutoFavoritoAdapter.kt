package br.com.atlantasistemas.market_w.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.atlantasistemas.market_w.R
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.ui.diff.ProdutoDiffCallback
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener

class ProdutoFavoritoAdapter(
    private val listener: ProdutoClickedListener,
) : ListAdapter<Produtos, ProdutoFavoritoAdapter.ProdutoFavoritoViewHolder>(ProdutoDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProdutoFavoritoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_produto_favorite, parent, false)
        return ProdutoFavoritoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProdutoFavoritoViewHolder,
        position: Int
    ) {
        val produto = getItem(position)

        holder.txtNomeProduto.text = produto.nomeProduto
        holder.txtUnProduto.text = produto.descricao
        holder.txtValorProduto.text = produto.valor.toString()

        holder.itemView.setOnClickListener {
            listener.produtoClickerListener(produto)
        }

    }


    class ProdutoFavoritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNomeProduto: TextView = itemView.findViewById(R.id.card_desc_produto_favorito)
        val txtUnProduto: TextView = itemView.findViewById(R.id.card_un_produto_favorito)
        val txtValorProduto: TextView = itemView.findViewById(R.id.card_valor_produto_favorito)
    }
}