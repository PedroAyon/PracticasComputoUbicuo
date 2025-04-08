package dev.pedroayon.googlemapsintegration.domain.repository

import dev.pedroayon.googlemapsintegration.domain.model.Store

interface StoreRepository {
    fun getStores(): List<Store>
    fun getStoreById(id: Int): Store?
}