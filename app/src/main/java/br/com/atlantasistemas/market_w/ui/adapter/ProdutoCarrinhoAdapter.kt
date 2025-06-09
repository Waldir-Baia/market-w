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
import br.com.atlantasistemas.market_w.ui.interface_listener.DecrementButtonClickListener
import br.com.atlantasistemas.market_w.ui.interface_listener.IncrementButtonClickListener
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import br.com.atlantasistemas.market_w.utils.Utils.formatarParaRealBrasileiro
import coil.load
import com.google.android.material.button.MaterialButton


class ProdutoCarrinhoAdapter(
    private val listener: ProdutoClickedListener,
    private val incrementButtonClickListener: IncrementButtonClickListener,
    private val decrementButtonClickListener: DecrementButtonClickListener
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
        holder.txtPrecoProduto.text = formatarParaRealBrasileiro(produto.valor)


        holder.imgProduto.load(produto.imageProduto){
            placeholder(R.drawable.icon_inventory)
            error(R.drawable.icon_cloud)
        }

        holder.itemView.setOnClickListener {
            listener.produtoClickerListener(produto)
        }

        holder.btnDecrement.setOnClickListener {
            decrementButtonClickListener.onDecrementButtonClick(produto)
        }

        holder.btnIncrement.setOnClickListener {
            incrementButtonClickListener.onIncrementButtonClick(produto)
        }

    }


    class ProdutoCarrinhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtNomeProduto: TextView = itemView.findViewById(R.id.desc_produto)
        val txtDescricaoProduto: TextView = itemView.findViewById(R.id.un_medida)
        val txtPrecoProduto: TextView = itemView.findViewById(R.id.preco_carrinho)
        val imgProduto: ImageView = itemView.findViewById(R.id.img_card_produto_carrinho)
        val btnDecrement: MaterialButton = itemView.findViewById(R.id.btn_card_favorite_decrement)
        val btnIncrement: MaterialButton = itemView.findViewById(R.id.btn_card_favorite_increment)


    }
}