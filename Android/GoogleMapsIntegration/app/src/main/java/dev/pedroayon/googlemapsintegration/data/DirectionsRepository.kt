package dev.pedroayon.googlemapsintegration.data

import com.google.android.gms.maps.model.LatLng
import dev.pedroayon.googlemapsintegration.utils.decodePolyline

class DirectionsRepository(private val api: GoogleDirectionsApi) {
    suspend fun getRoute(origin: LatLng, destination: LatLng, apiKey: String): List<LatLng> {
        val originStr = "${origin.latitude},${origin.longitude}"
        val destinationStr = "${destination.latitude},${destination.longitude}"
        val response = api.getDirections(originStr, destinationStr, "driving", apiKey)
        if (response.routes.isNotEmpty()) {
            val points = response.routes[0].overview_polyline.points
            return decodePolyline(points)
        }
        return emptyList()
    }
}