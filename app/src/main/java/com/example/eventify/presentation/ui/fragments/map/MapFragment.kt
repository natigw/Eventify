package com.example.eventify.presentation.ui.fragments.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.EventAPI
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.databinding.FragmentMapBinding
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate) {

    @Inject
    lateinit var venueApi: VenueAPI

    @Inject
    lateinit var eventApi: EventAPI

    private val locationPermissionRequestCode = 1

    private val sharedViewModel by activityViewModels<SharedViewModel>()

//    private val args by navArgs<TestMapFragmentArgs>()

    private lateinit var googleMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->

        this.googleMap = googleMap

        sharedViewModel.sharedCoordinates?.let {
            googleMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            it.lat.toDouble(),
                            it.long.toDouble()
                        )
                    )
                    .title(it.name)
                    .snippet(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
        }

//        // Load the custom style (dark mode)
//        try {
//            val success = googleMap.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(
//                    requireContext(),
//                    R.raw.map_dark_mode // Replace with your actual file name
//                )
//            )
//            if (!success) {
//                Log.e("MapStyle", "Style parsing failed.")
//            }
//        } catch (e: Resources.NotFoundException) {
//            Log.e("MapStyle", "Can't find style. Error: ", e)
//        }


        lifecycleScope.launch {
            try {
                // Adding markers
                val venues = this@MapFragment.venueApi.getAllVenues()
                val events = this@MapFragment.eventApi.getAllEvents()
                venues.forEach {
                    if (it.lat != "string") {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(it.lat.toDouble(), it.lng.toDouble()))
                                .title(it.name)
                                .snippet(it.description)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )
                        events.forEach { event ->
                            if (event.title.trim().toLowerCase() == it.name.trim().toLowerCase()) {
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(it.lat.toDouble(), it.lng.toDouble()))
                                        .title(it.name)
                                        .snippet(it.description)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                NancyToast.makeText(
                    requireContext(),
                    "$e",
                    NancyToast.LENGTH_SHORT,
                    NancyToast.ERROR,
                    false
                ).show()
                Log.e("exceptiona", e.toString())
            }
            // Marker click listener
            googleMap.setOnMarkerClickListener { marker ->
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
                marker.showInfoWindow()
                true
            }

            // Fetch and display route
            fetchRoute(
                LatLng(40.39367150806999, 49.86140788246094),
                LatLng(40.369909540241444, 49.8397321274377)
            )
        }

        binding.buttonSwitchMode.setOnClickListener {
            googleMap.mapType =
                if (googleMap.mapType == GoogleMap.MAP_TYPE_HYBRID) GoogleMap.MAP_TYPE_NORMAL else GoogleMap.MAP_TYPE_HYBRID
        }
        googleMap.uiSettings.setAllGesturesEnabled(true)
//        googleMap.uiSettings.isMyLocationButtonEnabled = false
        getMyLocation()  //show user's current location

    }

    override fun onViewCreatedLight() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

//        binding.map.post {  //no need anymore
//            val bottomNavHeight = requireActivity().findViewById<View>(R.id.bottomNavigationView).height
//            val params = binding.map.layoutParams as ConstraintLayout.LayoutParams
//            params.bottomMargin = bottomNavHeight
//            binding.map.layoutParams = params
//        }

    }

    private suspend fun fetchRoute(origin: LatLng, destination: LatLng) {
        val directionsApiKey =
            "AIzaSyDov61U_ntrpE8N7dfJ5ARWbKIeMwqFIjw"  //TODO -> bunu local.propertiesden gotur
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
                    Log.e("exceptiona", e.toString())
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
//                googleMap.addMarker(   //oldugun yere pin de qoysun? deqiqlesdir
//                    MarkerOptions()
//                        .position(currentLatLng)
//                        .title("Your current location")
//                        .snippet("You are here")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            } else {
                val bakuCityCenter = LatLng(40.3791, 49.8468)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bakuCityCenter, 12f))
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

//    override fun onStart() {
//        super.onStart()
//        mapFragment?.onStart()  // Forward onStart to the map fragment
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mapFragment?.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapFragment?.onPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mapFragment?.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapFragment?.onDestroy()
//    }
//
//    override fun onLowMemory() {    //TODO -> Deprecated, replace with onTrimMemory
//        super.onLowMemory()
//        mapFragment?.onLowMemory()
//    }

}