package dev.pedroayon.googlemapsintegration.presentation.screens

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dev.pedroayon.googlemapsintegration.BuildConfig
import dev.pedroayon.googlemapsintegration.data.DirectionsRepository
import dev.pedroayon.googlemapsintegration.data.GoogleDirectionsApi
import dev.pedroayon.googlemapsintegration.data.RetrofitClient
import dev.pedroayon.googlemapsintegration.presentation.viewmodel.StoreViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: StoreViewModel,
    storeId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    // Solicitar el permiso de ubicación
    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    // Actualizar la ubicación actual usando FusedLocationProviderClient si se tiene permiso
    if (locationPermissionState.status.isGranted) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        LaunchedEffect(locationPermissionState.status.isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.updateCurrentLocation(location.latitude, location.longitude)
                }
            }
        }
    }


    val currentLocation by viewModel.currentLocation.collectAsState()
    val store = viewModel.getStoreById(storeId)

    // Estado para almacenar la ruta (lista de puntos LatLng)
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

// Obtener la ruta de conducción utilizando la Google Directions API
    LaunchedEffect(currentLocation, store) {
        if (store != null) {
            val directionsApi = RetrofitClient.retrofit.create(GoogleDirectionsApi::class.java)
            val directionsRepository = DirectionsRepository(directionsApi)
            routePoints = directionsRepository.getRoute(
                LatLng(currentLocation.first, currentLocation.second),
                LatLng(store.latitude, store.longitude),
                BuildConfig.MAPS_API_KEY
            )
        }
    }
    if (store == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Local") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) {
            Text("Local no encontrado", modifier = Modifier.fillMaxSize())
        }
        return
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(currentLocation.first, currentLocation.second), 14f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(store.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Marcador para la ubicación actual
            Marker(
                state = MarkerState(position = LatLng(currentLocation.first, currentLocation.second)),
                title = "Tu Ubicación"
            )
            // Marcador para la ubicación del local
            Marker(
                state = MarkerState(position = LatLng(store.latitude, store.longitude)),
                title = store.name
            )
            // Dibuja la ruta de conducción si se obtuvo la lista de puntos
            if (routePoints.isNotEmpty()) {
                Polyline(
                    points = routePoints,
                    color = MaterialTheme.colorScheme.primary,
                    width = 5f
                )
            }
        }
    }
}