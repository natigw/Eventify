package com.example.eventify.presentation.ui.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.eventify.R
import com.example.common.utils.NancyToast
import com.example.data.remote.api.EventAPI
import com.example.data.remote.api.VenueAPI
import com.example.eventify.databinding.FragmentMapBinding
import com.example.eventify.presentation.ui.activities.OnBoardingActivity
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
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

    private var currentLatLng: LatLng? = null

    private val markers = mutableListOf<Marker>()

    @SuppressLint("PotentialBehaviorOverride")
    private val callback = OnMapReadyCallback { googleMap ->

        this.googleMap = googleMap

        sharedViewModel.sharedCoordinates?.let {
            removeMarkersAtLocation(it.lat, it.long)
            addMarker(
                lat = it.lat,
                lng = it.long,
                title = it.name,
                placeId = it.placeId,
                hue = if (it.placeType == "venue") BitmapDescriptorFactory.HUE_MAGENTA else BitmapDescriptorFactory.HUE_ORANGE
            )
        }

        lifecycleScope.launch {
            sharedViewModel.sharedRouteDestinationCoordinates.collect { destinationCoordinates ->
                destinationCoordinates?.let {
                    if (currentLatLng != null) {
                        fetchRoute(currentLatLng!!, LatLng(it.latitude, it.longitude))
                    } else {
                        NancyToast.makeText(
                            requireContext(),
                            "Please enable location service!",
                            NancyToast.LENGTH_SHORT,
                            NancyToast.WARNING,
                            false
                        ).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            try {
                // Adding markers
                val venues = this@MapFragment.venueApi.getAllVenues().body()
                val events = this@MapFragment.eventApi.getAllEvents().body()
                venues?.forEach {
                    if (it.lat != "string") {  //it.lat != 0.0   //viewmodeli qosandan sonra bele olmalidi
                        addMarker(
                            lat = it.lat.toDouble(),
                            lng = it.lng.toDouble(),
                            title = it.name,
                            hue = BitmapDescriptorFactory.HUE_AZURE,
                            placeId = it.id
                        )
                    }
                }
                events?.forEach {
                    if (it.location.lat != "string") {
                        addMarker(
                            lat = it.location.lat.toDouble(),
                            lng = it.location.lng.toDouble(),
                            title = it.event.title,
                            hue = BitmapDescriptorFactory.HUE_RED,
                            placeId = it.event.venueId,
                            placeType = "event"
                        )
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
                Log.e("exception", e.toString())
            }

            addMarker(
                40.38,
                49.85,
                "natig's custom event",
                BitmapDescriptorFactory.HUE_GREEN,
                12,
                "event"
            )

            // Marker click listener
            googleMap.setOnMarkerClickListener { marker ->
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(marker.position.latitude-0.005, marker.position.longitude), 15f))
                marker.showInfoWindow()

                val tags = marker.tag as List<*>
                val placeId = tags[0] as Int
                val placeType = tags[1] as String
                placeId.let {
                    findNavController().navigate(MapFragmentDirections.actionMapFragmentToMarkerDetailsBottomSheet(placeId, placeType))
                }
                true
            }
        }

        binding.buttonSwitchMode.setOnClickListener {
            googleMap.mapType =
                if (googleMap.mapType == GoogleMap.MAP_TYPE_HYBRID) GoogleMap.MAP_TYPE_NORMAL else GoogleMap.MAP_TYPE_HYBRID
        }
        googleMap.uiSettings.setAllGesturesEnabled(true)
//        googleMap.uiSettings.isMyLocationButtonEnabled = false
        getMyLocation()  //show user's current location

    }

    //TODO -> burda nese sehv olub
    private fun isDarkModeEnabled(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }


    private fun applyDynamicMapStyle(googleMap: GoogleMap) {
        val isDarkMode = isDarkModeEnabled(requireContext())
        val styleRes = if (isDarkMode) R.raw.map_aubergine else GoogleMap.MAP_TYPE_NORMAL
        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), styleRes))
        } catch (e: Resources.NotFoundException) {
            Log.e("MapStyle", "Error loading map style: ${e.message}")
        }
    }

    override fun onViewCreatedLight() {
        setDrawer()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (::googleMap.isInitialized) {
            applyDynamicMapStyle(googleMap)
        }
    }

    private fun addMarker(
        lat: Double,
        lng: Double,
        title: String,
        hue: Float,
        placeId: Int,
        placeType: String = "venue"
    ) {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(hue))
        )
        marker?.tag = listOf(placeId, placeType)
        marker?.let { markers.add(it) }
    }

    private fun removeMarkersAtLocation(lat: Double, lng: Double) {
        // Use iterator to avoid ConcurrentModificationException
        val iterator = markers.iterator()
        while (iterator.hasNext()) {
            val marker = iterator.next()
            if (marker.position == LatLng(lat, lng)) {
                marker.remove()   // Remove marker from the map
                iterator.remove() // Remove marker from the list
            }
        }
    }

    private suspend fun fetchRoute(origin: LatLng, destination: LatLng) {
        val directionsApiKey = com.example.eventify.BuildConfig.MAPS_API_KEY
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
                    Log.e("exception", e.toString())
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
                currentLatLng = LatLng(location.latitude, location.longitude)
                //oldugun yere pin de qoysun? deqiqlesdir
                addMarker(location.latitude, location.longitude, "You are here", BitmapDescriptorFactory.HUE_RED, 0)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 15f))
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

    private fun setDrawer() {

        binding.buttonDrawer.setOnClickListener {
            binding.myDrawerLayout.openDrawer(Gravity.LEFT, true)
        }

        val drawerLayout = binding.myDrawerLayout
        val navigationView: NavigationView = requireActivity().findViewById(R.id.drawerNavigationHome)

        val username = "username temp" //shprefLoggedin.getString("username", null)
        val useremail = "email temp" //shprefLoggedin.getString("email", null)

        val headerView = navigationView.getHeaderView(0)
        val userProfilePicture = headerView.findViewById<ShapeableImageView>(R.id.imageUserProfilePictureDrawer)
        val usernameDrawer = headerView.findViewById<TextView>(R.id.textUsernameDrawer)
        val userEmailDrawer = headerView.findViewById<TextView>(R.id.textUserEmailDrawer)
        val editProfileDrawer = headerView.findViewById<Button>(R.id.buttonEditProfileDrawer)


        usernameDrawer.text = username
        userEmailDrawer.text = useremail

        Glide.with(userProfilePicture)
            .load(getProfilePictureUri())
            .placeholder(R.drawable.usersample)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(userProfilePicture)

        editProfileDrawer.setOnClickListener {
            NancyToast.makeText(requireContext(), "edit", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
            //TODO -> edit screen
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    //TODO -> profile info and edit screen
                    true
                }
                R.id.drawer_referral -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.referralFragment)
                    true
                }
                R.id.drawer_subscription -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(R.id.subscriptionFragment)
                    true
                }
                R.id.drawer_tickets -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    //findNavController().navigate(R.id.)
                    //TODO -> current tickets
                    true
                }
                R.id.drawer_payment_history -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    //findNavController().navigate(R.id.)
                    //TODO -> all previous payments and active tickets
                    true
                }
                R.id.drawer_settings -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    //findNavController().navigate(R.id.)
                    //TODO -> profile info and edit screen
                    true
                }
                R.id.drawer_logout -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    //shprefLoggedin.edit().putBoolean("status", false).apply()
                    NancyToast.makeText(requireContext(), "You have been logged out!", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
                    navigateToAuthActivity()
                    true
                }
                else -> false
            }
        }
    }

    private fun getProfilePictureUri(): Uri {
        val uriImage = null//shprefProfilePicture.getString("profile_image_uri", null)
        return if (uriImage == null) Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.usersample}")
        else Uri.parse(uriImage)
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}