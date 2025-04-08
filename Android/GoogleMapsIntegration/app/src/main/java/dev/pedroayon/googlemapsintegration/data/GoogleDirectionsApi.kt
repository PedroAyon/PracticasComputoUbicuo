package dev.pedroayon.googlemapsintegration.data

import dev.pedroayon.googlemapsintegration.domain.model.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApi {
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String = "driving",
        @Query("key") key: String
    ): DirectionsResponse
}