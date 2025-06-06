package br.com.atlantasistemas.market_w.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.atlantasistemas.market_w.Detalhes_Produto_Activity
import br.com.atlantasistemas.market_w.R
import br.com.atlantasistemas.market_w.Utils.dadosProdutoGlobal
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.databinding.FragmentMainBinding
import br.com.atlantasistemas.market_w.ui.MainActivityViewModel
import br.com.atlantasistemas.market_w.ui.MapsViewModel
import br.com.atlantasistemas.market_w.ui.adapter.ProdutoAdapter
import br.com.atlantasistemas.market_w.ui.adapter.ProdutoFavoritoAdapter
import br.com.atlantasistemas.market_w.ui.interface_listener.AddButtonClickListener
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var adapterPromocao: ProdutoAdapter
    private lateinit var adapterMaisVendidos: ProdutoAdapter
    private lateinit var adapterMenosVendidos: ProdutoAdapter

    // NOVO: Adapter ESPECÍFICO para o RecyclerView dentro do SearchView
    private lateinit var searchViewAdapter: ProdutoFavoritoAdapter

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainActivityViewModel by activityViewModels()
    //private val mapsViewModel: MapsViewModel by viewModels()

    // NOVO: Lista que conterá todos os produtos disponíveis para pesquisa
    private var allAvailableProducts: MutableList<Produtos> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dadosProdutoGlobal = MutableLiveData<Produtos>()

        // Seus RecyclerViews principais - mantidos inalterados
        binding.recyclerPromocao.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMaisVendidos.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMenosVendidos.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapterPromocao =
            ProdutoAdapter(createProdutoClickedListener(), addButtonProdutoClickedListener())
        adapterMaisVendidos =
            ProdutoAdapter(createProdutoClickedListener(), addButtonProdutoClickedListener())
        adapterMenosVendidos =
            ProdutoAdapter(createProdutoClickedListener(), addButtonProdutoClickedListener())

        binding.recyclerPromocao.adapter = adapterPromocao
        binding.recyclerMaisVendidos.adapter = adapterMaisVendidos
        binding.recyclerMenosVendidos.adapter = adapterMenosVendidos

        searchViewAdapter =
            ProdutoFavoritoAdapter(createProdutoClickedListener()) // Reutiliza o listener de clique

        binding.searchView.findViewById<RecyclerView>(R.id.search_results_list).apply {
            layoutManager = LinearLayoutManager(requireContext()) // 2 cards por linha
            adapter = searchViewAdapter
        }

        binding.searchView.setupWithSearchBar(binding.mainSearchBar)

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    updateSearchSuggestions(query) // Chama a função para atualizar as sugestões enquanto digita
                } else {
                    showInitialSearchSuggestions() // Mostra sugestões iniciais quando o campo está vazio
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchView.editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchView.text.toString()
                performSearch(query) // Chama a função para executar a pesquisa principal
                binding.searchView.hide() // Esconde o SearchView após a pesquisa
                true // Indica que o evento foi consumido
            }
            false // Indica que o evento não foi consumido (se não for IME_ACTION_SEARCH)
        }

        binding.searchView.addTransitionListener { searchView, previousState, newState ->
            if (newState == SearchView.TransitionState.SHOWN) {
                // Quando o SearchView é totalmente expandido e visível
                showInitialSearchSuggestions()
            }
        }
        configureObservers()
    }

    private fun createProdutoClickedListener(): ProdutoClickedListener {
        return object : ProdutoClickedListener {
            override fun produtoClickerListener(viewProduto: Produtos) {
                dadosProdutoGlobal.value = viewProduto
                startActivity(Intent(requireContext(), Detalhes_Produto_Activity::class.java))
            }
        }
    }

    private fun addButtonProdutoClickedListener(): AddButtonClickListener {
        return object : AddButtonClickListener {
            override fun onAddButtonClick(viewProduto: Produtos) {
                Toast.makeText(
                    context,
                    "Produto Adicionado: ${viewProduto.nomeProduto}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Seu método configureObservers - ALTERADO para popular 'allAvailableProducts'
    private fun configureObservers() {
        viewModel.produtoPromocao.observe(viewLifecycleOwner) { result ->
            atualizaUiProdutoPromocao(result)
            allAvailableProducts.addAll(result.filter { it !in allAvailableProducts })
        }
        viewModel.produtoPromocao.observe(viewLifecycleOwner) { result ->
            adapterMaisVendidos.submitList(result)
            allAvailableProducts.addAll(result.filter { it !in allAvailableProducts })
        }
        viewModel.produtoPromocao.observe(viewLifecycleOwner) { result ->
            adapterMenosVendidos.submitList(result)
            allAvailableProducts.addAll(result.filter { it !in allAvailableProducts })
        }

//        mapsViewModel.cityName.observe(viewLifecycleOwner) { result ->
//            Log.d("WBN_Maps", "${result}")
//            binding.locationText.text = result
//        }

    }

    private fun atualizaUiProdutoPromocao(produtos: List<Produtos>) {
        adapterPromocao.submitList(produtos)
    }

    private fun showInitialSearchSuggestions() {
        val initialSuggestions = allAvailableProducts.shuffled().take(5)
        searchViewAdapter.submitList(initialSuggestions)
    }

    private fun updateSearchSuggestions(query: String) {
        val filteredSuggestions = allAvailableProducts.filter { produto ->
            produto.nomeProduto.contains(query, ignoreCase = true) ||
                    produto.descricao.contains(query, ignoreCase = true)

        }.distinct()
        searchViewAdapter.submitList(filteredSuggestions)
    }

    private fun performSearch(query: String) {
        // Esta é a lógica final da sua pesquisa. O que você quer que aconteça?
        // Opções comuns:
        // 1. Chamar um método no seu ViewModel para buscar produtos no backend com essa `query`.
        //    Ex: viewModel.searchProducts(query)
        // 2. Navegar para uma nova Activity/Fragment de resultados de pesquisa, passando a `query` como argumento.
        //    Ex: findNavController().navigate(R.id.action_MainFragment_to_SearchResultsFragment, bundleOf("searchQuery" to query))
        // 3. (Se os resultados forem exibidos na mesma tela) Filtrar os RecyclerViews existentes
        //    ou atualizar um RecyclerView específico de resultados que esteja visível na tela principal.
        //    Ex: adapterPromocao.submitList(allAvailableProducts.filter { it.nomeProduto.contains(query, ignoreCase = true) })

        Toast.makeText(context, "Pesquisa final acionada por: '$query'", Toast.LENGTH_LONG).show()

        // Exemplo de como você poderia atualizar os adapters principais se os resultados da busca
        // fossem para serem mostrados nos RecyclerViews da tela principal (isso é apenas um exemplo,
        // geralmente você teria uma lógica mais complexa para resultados de busca completos):
        // adapterPromocao.submitList(allAvailableProducts.filter { it.nomeProduto.contains(query, ignoreCase = true) })
        // adapterMaisVendidos.submitList(emptyList()) // Limpar outros, talvez
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks
    }

    // Seu Companion Object - mantido inalterado
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