package br.com.atlantasistemas.market_w.ui

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.atlantasistemas.market_w.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel // Anote a ViewModel com @HiltViewModel
class MapsViewModel @Inject constructor( // Use @Inject no construtor
    private val locationRepository: LocationRepository,
    // @ApplicationContext é injetado automaticamente para ViewModels se você precisar do Context
    @dagger.hilt.android.qualifiers.ApplicationContext private val applicationContext: Context
) : ViewModel() { // Mude para ViewModel, não mais AndroidViewModel

    val cityName: MutableLiveData<String> = MutableLiveData()

    init {
        val hash = this.hashCode() // Obtenha o hash para identificar a instância
        Log.d("WBN_Maps", "MapsViewModel instanciada. Hash: $hash")
    }

    fun getUserCity() {
        viewModelScope.launch {
            val location: Location? = locationRepository.getLocalizacao()

            if (location != null) {
                val city = getCityNameFromLocation(location)
                cityName.postValue(city)
            } else {
                cityName.postValue("Permissão de localização necessária ou localização não disponível")
            }
        }
    }

    private fun getCityNameFromLocation(location: Location): String {
        // Use o applicationContext injetado para o Geocoder
        val geocoder = Geocoder(applicationContext, Locale("pt", "BR"))
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                Log.d("WBN_Maps", "${addresses}")
                val address = addresses[0]
                val city = address.locality ?: "${address.subAdminArea} - ${address.adminArea ?: ""}"
                return city.ifBlank { "Cidade desconhecida" }

            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Erro ao obter cidade (problema de rede/geocoding)"
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return "Erro ao obter cidade (coordenadas inválidas)"
        }
        return "Cidade não encontrada"
    }
}