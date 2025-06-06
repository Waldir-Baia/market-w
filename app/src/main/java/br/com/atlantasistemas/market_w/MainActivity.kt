package br.com.atlantasistemas.market_w

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.databinding.ActivityMainBinding
import br.com.atlantasistemas.market_w.ui.MainActivityViewModel
import br.com.atlantasistemas.market_w.ui.MapsViewModel
import br.com.atlantasistemas.market_w.ui.adapter.FavoriteFragment
import br.com.atlantasistemas.market_w.ui.adapter.ProdutoAdapter
import br.com.atlantasistemas.market_w.ui.fragment.CartFragment
import br.com.atlantasistemas.market_w.ui.fragment.MainFragment
import br.com.atlantasistemas.market_w.ui.fragment.SearchFragment
import br.com.atlantasistemas.market_w.ui.interface_listener.AddButtonClickListener
import br.com.atlantasistemas.market_w.ui.interface_listener.ProdutoClickedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterProduto: ProdutoAdapter

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carrega o fragmento inicial se ainda não estiver carregado
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, MainFragment())
                .commit()
            // Marcar o item inicial como selecionado no BottomNavigation
            binding.bottomNavigation.selectedItemId = R.id.icon_store
        }

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.findNavController()

        Log.d("WBN_MainActivity", "NavController: $navController")

        iniciarFormulario()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when(item.itemId){
                R.id.icon_store -> MainFragment()
                R.id.icon_search -> SearchFragment()
                R.id.icon_cart -> CartFragment()
                R.id.icon_favorite -> FavoriteFragment()
                R.id.icon_account -> MainFragment()
                else -> MainFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
            true
        }
    }

    private fun iniciarFormulario() {
//        binding.recyclerPromocao.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerMaisVendidos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerMenosVendidos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        configureObservers()

    }

    private fun configureObservers() {

        viewModel.produtoPromocao.observe(this) { result ->
            atualizaUiProdutoPromocao(result)
        }

    }

    private fun atualizaUiProdutoPromocao(produtos: List<Produtos>) {
        if (!::adapterProduto.isInitialized) {
            adapterProduto = ProdutoAdapter(
                object : ProdutoClickedListener {
                    override fun produtoClickerListener(viewProduto: Produtos) {

                    }
                },
                object : AddButtonClickListener{
                    override fun onAddButtonClick(produto: Produtos) {
                        Toast.makeText(this@MainActivity, "${produto.nomeProduto}", Toast.LENGTH_LONG).show()
                    }
                }
            )
//            binding.recyclerPromocao.adapter = adapterProduto
//            binding.recyclerMaisVendidos.adapter = adapterProduto
//            binding.recyclerMenosVendidos.adapter = adapterProduto
        }
        adapterProduto.submitList(produtos)
    }


}