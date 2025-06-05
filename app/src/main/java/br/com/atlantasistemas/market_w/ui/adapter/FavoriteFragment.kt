package br.com.atlantasistemas.market_w.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.databinding.FragmentFavoriteBinding
import br.com.atlantasistemas.market_w.ui.MainActivityViewModel
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private lateinit var adapter: ProdutoFavoritoAdapter

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureObservers()

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks
    }

    private fun configureObservers() {
        viewModel.produtoPromocao.observe(viewLifecycleOwner) { result ->
            atualizaUIProdutoFavorito(result)
        }

    }

    private fun atualizaUIProdutoFavorito(produtos: List<Produtos>){
        if(!::adapter.isInitialized){
            adapter = ProdutoFavoritoAdapter(object : ProdutoClickedListener {
                override fun produtoClickerListener(viewProduto: Produtos) {

                }
            })
            binding.recyclerViewFavorite.adapter = adapter
        }
        adapter.submitList(produtos)
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}