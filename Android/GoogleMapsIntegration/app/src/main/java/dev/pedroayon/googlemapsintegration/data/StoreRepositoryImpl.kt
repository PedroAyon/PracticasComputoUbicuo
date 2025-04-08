package dev.pedroayon.googlemapsintegration.data

import dev.pedroayon.googlemapsintegration.domain.model.Store
import dev.pedroayon.googlemapsintegration.domain.repository.StoreRepository

class StoreRepositoryImpl : StoreRepository {

    // Lista de locales ficticios
    private val stores = listOf(
        Store(1, "Local A", 22.276660603355268, -97.87487807482464),
        Store(2, "Local B", 22.279288126933302, -97.79752163631504),
        Store(3, "Local C", 22.243865431239787, -97.83861147087892),
        Store(4, "Local D", 22.216099612024237, -97.85927923129967),
        Store(5, "Local E", 22.248833268283203, -97.8639830900124),
        Store(6, "Local F", 22.298556438261265, -97.87629426151426),
    )

    override fun getStores(): List<Store> = stores

    override fun getStoreById(id: Int): Store? = stores.find { it.id == id }
}