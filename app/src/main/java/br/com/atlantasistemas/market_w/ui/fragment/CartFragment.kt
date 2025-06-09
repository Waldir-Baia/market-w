package br.com.atlantasistemas.market_w.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.databinding.FragmentCartBinding
import br.com.atlantasistemas.market_w.ui.MainActivityViewModel
import br.com.atlantasistemas.market_w.ui.adapter.ProdutoCarrinhoAdapter
import br.com.atlantasistemas.market_w.ui.interface_listener.DecrementButtonClickListener
import br.com.atlantasistemas.market_w.ui.interface_listener.IncrementButtonClickListener
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var adapterProduto: ProdutoCarrinhoAdapter

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container ,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureObservers()


        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun configureObservers(){
        viewModel.carregarCarrinho()


    }

    private fun atualizaUiProdutoPromocao(produtos: List<Produtos>) {
        if (!::adapterProduto.isInitialized) {
            adapterProduto = ProdutoCarrinhoAdapter(
                object : ProdutoClickedListener {
                    override fun produtoClickerListener(viewProduto: Produtos) {

                }
            },
                object : IncrementButtonClickListener{
                    override fun onIncrementButtonClick(produtos: Produtos) {
                        viewModel.incrementar(produtos)
                    }
                },

                object : DecrementButtonClickListener{
                    override fun onDecrementButtonClick(produtos: Produtos) {
                        TODO("Not yet implemented")
                    }

                }

            )
            binding.recyclerViewCart.adapter = adapterProduto
        }
        adapterProduto.submitList(produtos)
    }
}