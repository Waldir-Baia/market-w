package br.com.atlantasistemas.market_w.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import br.com.atlantasistemas.market_w.ui.interfac.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationRepository @Inject constructor
    (
    @dagger.hilt.android.qualifiers.ApplicationContext
    private val context: Context
) : LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getLocalizacao(): Location? {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissão não concedida, você deve lidar com isso na Activity/Fragment
            return null
        }

        return try {
            // Tenta pegar a última localização conhecida primeiro
            val lastLocation =
                fusedLocationClient.lastLocation.await() // Isso requer uma extensão de coroutine
            if (lastLocation != null) {
                lastLocation
            } else {
                // Se a última localização não estiver disponível, solicita uma nova
                requestSingleLocationUpdate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun requestSingleLocationUpdate(): Location? {
        return callbackFlow {
            val locationRequest = LocationRequest.Builder(10000) // Intervalo de 10 segundos
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxUpdates(1) // Queremos apenas uma atualização
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        trySend(it).isSuccess // Envia a localização e marca como sucesso
                    }
                    fusedLocationClient.removeLocationUpdates(this) // Remove as atualizações após receber uma
                    close() // Fecha o Flow
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }.first() // Pega apenas o primeiro valor emitido
    }

    suspend fun <T> Task<T>.await(): T {
        return suspendCoroutine { continuation ->
            addOnSuccessListener { result ->
                continuation.resume(result)
            }
            addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
            addOnCanceledListener {
                continuation.resumeWithException(java.util.concurrent.CancellationException("Task cancelled"))
            }
        }
    }
}