package com.example.menzago.ui.screens.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.menzago.data.location.DistanceUtils
import com.example.menzago.data.location.LocationRepository
import com.example.menzago.data.mock.MockData
import com.example.menzago.data.model.Canteen
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.example.menzago.data.location.UserLocationRepository
import android.graphics.Color
import org.osmdroid.views.overlay.IconOverlay
import com.example.menzago.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

@Composable
fun MapScreen(
    onOpenCanteen: (Int) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val osijekPoint = remember {
        GeoPoint(45.5540, 18.6955)
    }

    val locationRepository = remember {
        LocationRepository(context.applicationContext)
    }

    var infoText by remember { mutableStateOf("Pronađi najbližu menzu") }

    val mapView = remember {
        Configuration.getInstance().userAgentValue = context.packageName

        MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            setBuiltInZoomControls(false)
            isTilesScaledToDpi = true

            minZoomLevel = 12.0
            maxZoomLevel = 19.0

            controller.setZoom(15.0)
            controller.setCenter(osijekPoint)
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    fun scaledMarkerIcon(
        drawableRes: Int,
        width: Int = 64,
        height: Int = 64
    ): Drawable? {
        val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return null

        val bitmap = drawable.toBitmap()
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            false
        )

        return BitmapDrawable(context.resources, scaledBitmap)
    }

    LaunchedEffect(Unit) {
        mapView.overlays.clear()

        MockData.canteens.forEach { canteen ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(canteen.latitude, canteen.longitude)
                title = canteen.name
                snippet = canteen.location
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                icon = scaledMarkerIcon(
                    drawableRes = R.drawable.menza,
                    width = 56,
                    height = 56
                )

                setOnMarkerClickListener { _, _ ->
                    onOpenCanteen(canteen.id)
                    true
                }
            }

            mapView.overlays.add(marker)
        }

        mapView.invalidate()
    }

    fun isLocationReasonable(location: Location): Boolean {
        val result = FloatArray(1)

        Location.distanceBetween(
            location.latitude,
            location.longitude,
            osijekPoint.latitude,
            osijekPoint.longitude,
            result
        )

        UserLocationRepository.setLocation(location)

        return result[0] < 100_000
    }

    fun addUserMarkerAndCenter(
        latitude: Double,
        longitude: Double
    ) {
        mapView.overlays.removeAll {
            it is Marker && it.title == "Tvoja lokacija"
        }

        val point = GeoPoint(latitude, longitude)

        val marker = Marker(mapView).apply {
            position = point
            title = "Tvoja lokacija"
            snippet = "Ovdje se trenutno nalaziš"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            icon = scaledMarkerIcon(
                drawableRes = R.drawable.user_marker,
                width = 64,
                height = 64
            )
        }

        mapView.overlays.add(marker)
        mapView.controller.setZoom(16.0)
        mapView.controller.setCenter(point)
        mapView.invalidate()
    }

    fun formatDistance(meters: Float): String {
        return if (meters < 1000) {
            "${meters.toInt()} m"
        } else {
            "%.1f km".format(meters / 1000)
        }
    }

    fun loadCurrentLocation() {
        scope.launch {
            val location = locationRepository.getCurrentLocation()

            if (location == null) {
                infoText = "Lokacija trenutno nije dostupna."
                return@launch
            }

            if (!isLocationReasonable(location)) {
                infoText = "GPS emulatora nije u blizini Osijeka."
                mapView.controller.setZoom(15.0)
                mapView.controller.setCenter(osijekPoint)
                return@launch
            }

            val nearest = DistanceUtils.findNearestCanteen(
                userLocation = location,
                canteens = MockData.canteens
            )

            if (nearest != null) {
                val distance = DistanceUtils.distanceToCanteenMeters(
                    userLocation = location,
                    canteen = nearest
                )

                infoText = "Najbliža menza: ${nearest.name} • ${formatDistance(distance)}"
            } else {
                infoText = "Najbliža menza nije pronađena."
            }

            addUserMarkerAndCenter(
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            loadCurrentLocation()
        } else {
            infoText = "Dozvola za lokaciju nije odobrena."
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView
            }
        )

        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = infoText,
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            loadCurrentLocation()
                        } else {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Koristi moju lokaciju")
                }
            }
        }
    }
}