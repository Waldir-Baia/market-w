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


class ProdutoCarrinhoAdapter(
    private val listener: ProdutoClickedListener,
) : ListAdapter<Produtos, ProdutoCarrinhoAdapter.ProdutoCarrinhoViewHolder>(ProdutoDiffCallback){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProdutoCarrinhoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_produto_carrinho, parent, false)
        return ProdutoCarrinhoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProdutoCarrinhoViewHolder,
        position: Int
    ) {
        val produto = getItem(position)

        holder.txtNomeProduto.text = produto.nomeProduto
        holder.txtDescricaoProduto.text = produto.descricao
        holder.txtPrecoProduto.text = produto.valor.toString()

        holder.itemView.setOnClickListener {
            listener.produtoClickerListener(produto)
        }

    }


    class ProdutoCarrinhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNomeProduto: TextView = itemView.findViewById(R.id.desc_produto)
        val txtDescricaoProduto: TextView = itemView.findViewById(R.id.un_medida)
        val txtPrecoProduto: TextView = itemView.findViewById(R.id.preco_carrinho)

    }
}