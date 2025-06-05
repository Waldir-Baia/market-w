package br.com.atlantasistemas.market_w.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.atlantasistemas.market_w.R
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.ui.diff.ProdutoDiffCallback
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import coil.load

class ProdutoAdapter(
    private val listener: ProdutoClickedListener,
) : ListAdapter<Produtos, ProdutoAdapter.ProdutoViewHolder>(ProdutoDiffCallback){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_produtos, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProdutoViewHolder,
        position: Int
    ) {
        val produto = getItem(position)

        holder.txtNomeProduto.text = produto.nomeProduto
        holder.txtDescricaoProduto.text = produto.descricao
        holder.txtPrecoProduto.text = produto.valor.toString()

        holder.imgProduto.load(produto.imageProduto){
            placeholder(R.drawable.icon_inventory)
            error(R.drawable.icon_cloud)
        }

        holder.itemView.setOnClickListener {
            listener.produtoClickerListener(produto)
        }

    }


    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNomeProduto: TextView = itemView.findViewById(R.id.product_title)
        val txtDescricaoProduto: TextView = itemView.findViewById(R.id.product_subtitle)
        val txtPrecoProduto: TextView = itemView.findViewById(R.id.product_price)
        val imgProduto: ImageView = itemView.findViewById(R.id.card_produto)

    }
}