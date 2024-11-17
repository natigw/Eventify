package com.example.eventify.presentation.ui.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.EventifyAPI
import com.example.eventify.databinding.FragmentMapBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate), OnMapReadyCallback {

    @Inject
    lateinit var api: EventifyAPI

    private lateinit var googleMap: GoogleMap

    private val locationPermissionRequestCode = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        binding.mapView.post {
            val bottomNavHeight = requireActivity().findViewById<View>(R.id.bottomNavigationView).height / 2
            val params = binding.mapView.layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin = bottomNavHeight
            binding.mapView.layoutParams = params
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        lifecycleScope.launch {
            // Adding markers
            val venues = api.getAllVenues()
            if (venues.size >= 2) {
                val firstVenue = venues[0]
                val secondVenue = venues[1]

                val firstMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(firstVenue.lat.toDouble(), firstVenue.lng.toDouble()))
                        .title("First Venue")
                        .snippet("Description for the first venue")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )

                val secondMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(secondVenue.lat.toDouble(), secondVenue.lng.toDouble()))
                        .title("Second Venue")
                        .snippet("Description for the second venue")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )

                // Marker click listener
                googleMap.setOnMarkerClickListener { marker ->
                    marker.showInfoWindow()
                    true
                }

                // Fetch and display route
                fetchRoute(
                    LatLng(firstVenue.lat.toDouble(), firstVenue.lng.toDouble()),
                    LatLng(secondVenue.lat.toDouble(), secondVenue.lng.toDouble())
                )
            }

            // Show user's current location
            getMyLocation()
        }
    }

    private suspend fun fetchRoute(origin: LatLng, destination: LatLng) {
        val directionsApiKey = "AIzaSyDov61U_ntrpE8N7dfJ5ARWbKIeMwqFIjw" // Replace with your API key
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&key=$directionsApiKey"

        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                val jsonResponse = JSONObject(response.body()!!.string())
                val routes = jsonResponse.getJSONArray("routes")

                if (routes.length() > 0) {
                    val points = routes.getJSONObject(0)
                        .getJSONObject("overview_polyline")
                        .getString("points")

                    val decodedPath = PolyUtil.decode(points)
                    withContext(Dispatchers.Main) {
                        googleMap.addPolyline(
                            PolylineOptions()
                                .addAll(decodedPath)
                                .color(Color.BLUE)
                                .width(10f)
                        )
                    }
                } else {

                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    NancyToast.makeText(
                        requireContext(),
                        "Error fetching route",
                        NancyToast.LENGTH_LONG,
                        NancyToast.ERROR,
                        false
                    )
                }
            }
        }
    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationPermissionRequestCode
            )
            return
        }

        googleMap.isMyLocationEnabled = true

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation()
            } else {
                NancyToast.makeText(
                    requireContext(),
                    "Location permission denied",
                    NancyToast.LENGTH_LONG,
                    NancyToast.ERROR,
                    false
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun observeChanges() {
        // Observe LiveData or other changes if needed
    }

    override fun onViewCreatedLight() {
        // Initialize light-specific views or features if needed
    }
}