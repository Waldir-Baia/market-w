package br.com.atlantasistemas.market_w.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.databinding.FragmentMainBinding
import br.com.atlantasistemas.market_w.ui.MainActivityViewModel
import br.com.atlantasistemas.market_w.ui.adapter.ProdutoAdapter
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var adapterProduto: ProdutoAdapter

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureObservers()

        //Aqui eu acesso as minhas views
        binding.recyclerPromocao.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMaisVendidos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMenosVendidos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


    }

    private fun configureObservers(){
        viewModel.produtoPromocao.observe(viewLifecycleOwner) { result ->
            atualizaUiProdutoPromocao(result)
        }

    }

    private fun atualizaUiProdutoPromocao(produtos: List<Produtos>) {
        if (!::adapterProduto.isInitialized) {
            adapterProduto = ProdutoAdapter(object : ProdutoClickedListener {
                override fun produtoClickerListener(viewProduto: Produtos) {

                }
            })
            binding.recyclerPromocao.adapter = adapterProduto
            binding.recyclerMaisVendidos.adapter = adapterProduto
            binding.recyclerMenosVendidos.adapter = adapterProduto
        }
        adapterProduto.submitList(produtos)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    // Pode adicionar os parâmetros aqui, se quiser usar mais tarde
                }
            }
    }
}
