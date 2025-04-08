package dev.pedroayon.googlemapsintegration.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pedroayon.googlemapsintegration.data.StoreRepositoryImpl
import dev.pedroayon.googlemapsintegration.domain.model.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel : ViewModel() {
    private val repository = StoreRepositoryImpl()

    // Se inicia con una ubicación simulada (ej. Googleplex) que luego se actualizará
    private val _currentLocation = MutableStateFlow(Pair(37.4219983, -122.084))
    val currentLocation: StateFlow<Pair<Double, Double>> = _currentLocation

    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores: StateFlow<List<Store>> = _stores

    init {
        viewModelScope.launch {
            _stores.value = repository.getStores()
        }
    }

    fun getStoreById(id: Int): Store? = repository.getStoreById(id)

    fun updateCurrentLocation(lat: Double, lng: Double) {
        _currentLocation.value = Pair(lat, lng)
    }
}