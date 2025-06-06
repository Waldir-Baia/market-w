package br.com.atlantasistemas.market_w

import br.com.atlantasistemas.market_w.utils.RemoveWhiteBackgroundTransformation
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.atlantasistemas.market_w.utils.Utils.formatarParaRealBrasileiro
import br.com.atlantasistemas.market_w.data.entities.Produtos
import br.com.atlantasistemas.market_w.databinding.ActivityDetalhesProdutoBinding
import br.com.atlantasistemas.market_w.ui.DetalhesProdutoViewModel
import coil.load
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Detalhes_Produto_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalhesProdutoBinding

    private val viewModel : DetalhesProdutoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetalhesProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.fundo_detalhe_produto)

        iniciarFormulario()
        configureObservers()

        binding.toolbar.setNavigationOnClickListener{
            startActivity(Intent(this@Detalhes_Produto_Activity, MainActivity::class.java))
        }

    }

    private fun iniciarFormulario(){

    }

    private fun configureObservers(){
        viewModel.dadosProdutoViewModel.observe(this) { result ->
            setaValoresUI(result)
        }
    }

    private fun setaValoresUI(produto: Produtos){
        binding.txtTitle.text = produto.nomeProduto
        binding.txtDetail.text = produto.descricao
        binding.txtPrice.text = formatarParaRealBrasileiro(produto.valor)

        binding.imgProduct.load(produto.imageProduto){
            placeholder(R.drawable.icon_inventory)
            error(R.drawable.icon_cloud)
            crossfade(true)
            transformations(RemoveWhiteBackgroundTransformation())
        }

    }

}


