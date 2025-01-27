package com.example.eventify.presentation.ui.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.crossfadeAppear
import com.example.common.utils.crossfadeDisappear
import com.example.common.utils.hideKeyboard
import com.example.common.utils.nancyToastError
import com.example.common.utils.showKeyboard
import com.example.eventify.R
import com.example.eventify.databinding.FragmentMapBinding
import com.example.eventify.presentation.adapters.MapSearchAdapter
import com.example.eventify.presentation.viewmodels.MapViewModel
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate), OnMapReadyCallback {

    private val viewModel by viewModels<MapViewModel>()

    private val locationPermissionRequestCode = 1

    private val sharedViewModel by activityViewModels<SharedViewModel>()

//    private val args by navArgs<TestMapFragmentArgs>()

    private lateinit var googleMap: GoogleMap

    private val markers = mutableListOf<Marker>()

    private val mapSearchAdapter = MapSearchAdapter {
        findNavController().navigate(MapFragmentDirections.actionMapFragmentToMarkerDetailsBottomSheet(
            placeId = it.placeId,
            placeType = it.placeType,
            lat = it.lat,
            lng = it.lng
        ))
        val marker = viewModel.findMarkerByLatLng(it.lat.toDouble(),it.lng.toDouble())
        marker?.showInfoWindow()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat.toDouble()-0.005, it.lng.toDouble()), 15f))
        binding.root.transitionToStart()
        crossfadeDisappear(binding.searchRV, duration = 300)
        crossfadeDisappear(binding.progressBar, 300)
        binding.editTextText.clearFocus()
    }


    override fun onViewCreatedLight() {
        setAdapters()
        motionLayout()
        observer()
        searchInputHandler()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.buttonLocationMap.setOnClickListener {
            getMyLocation()
        }

        binding.buttonSwitchMode.setOnClickListener {
            googleMap.mapType = if (googleMap.mapType == GoogleMap.MAP_TYPE_HYBRID) GoogleMap.MAP_TYPE_NORMAL else GoogleMap.MAP_TYPE_HYBRID
        }

        binding.cancelSearchButton.setOnClickListener{
            binding.editTextText.text = null
        }

        binding.backwardButton.setOnClickListener {
            binding.root.transitionToStart()
            crossfadeDisappear(binding.searchRV, duration = 300)
            crossfadeDisappear(binding.notFoundView, 300)
            crossfadeDisappear(binding.progressBar, 300)
            binding.editTextText.clearFocus()
            hideKeyboard(binding.editTextText)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.root.currentState == R.id.end){
                    binding.root.transitionToStart()
                    crossfadeDisappear(binding.searchRV, duration = 300)
                    crossfadeDisappear(binding.notFoundView, 300)
                    crossfadeDisappear(binding.progressBar, 300)
                    binding.editTextText.clearFocus()
                }
                else{
                    requireActivity().finish()
                }
            }
        })
    }


    private fun searchInputHandler(){
        binding.editTextText.imeOptions = EditorInfo.IME_ACTION_SEARCH


        binding.editTextText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(s: Editable?) {
                viewModel.inputState.update { s.toString() }
            }
        })
    }



    @OptIn(FlowPreview::class)
    private fun observer(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState
                .filterNotNull()
                .collectLatest{
                    binding.notFoundView.isVisible = it.isEmpty()
                    mapSearchAdapter.updateAdapter(it)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inputState
                .filterNotNull()
                .debounce(1000)
                .collectLatest {
                    if(it==""){
                        binding.notFoundView.isVisible = false
                        mapSearchAdapter.updateAdapter(emptyList())
                    }
                    else{
                        viewModel.searchPlaces(it)
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading
                .filterNotNull()
                .collectLatest {
                    binding.progressBar.isVisible = it



                    binding.searchRV.isVisible = !it
            }
        }
    }

    private fun setAdapters(){
        binding.searchRV.adapter = mapSearchAdapter
    }

    private fun motionLayout(){
        binding.root.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                Log.e("progress",progress.toString())
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(currentId == R.id.end) {
                    showKeyboard(binding.editTextText)
                    binding.editTextText.requestFocus()
                    binding.searchRV.isVisible = true
                    binding.searchRV.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start()
                    crossfadeAppear(binding.notFoundView, 300)
                    crossfadeAppear(binding.progressBar, 300)
                    binding.root.getTransition(R.id.myTransition).isEnabled = false
                }
                else if(currentId == R.id.start){
                    binding.root.getTransition(R.id.myTransition).isEnabled = true
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                Log.e("triggerId",triggerId.toString())

            }

        })
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
        marker?.let {
            viewModel.markerList.add(marker)
        }
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
        googleMap.uiSettings.isMyLocationButtonEnabled = false

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.currentLocation?.let {
                    removeMarkersAtLocation(viewModel.currentLocation!!.latitude, viewModel.currentLocation!!.longitude)
                }
                addMarker(
                    location.latitude,
                    location.longitude,
                    getString(R.string.you_are_here),
                    BitmapDescriptorFactory.HUE_RED,
                    0
                )
                viewModel.currentLocation = LatLng(location.latitude, location.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.currentLocation!!, 15f))
            } else {
                viewModel.currentLocation?.let {
                    removeMarkersAtLocation(viewModel.currentLocation!!.latitude, viewModel.currentLocation!!.longitude)
                }
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
                nancyToastError(requireContext(), getString(R.string.location_permission_denied))
            }
        }
    }


    @OptIn(FlowPreview::class)
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {

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


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.venuesState
                .filterNotNull()
                .collectLatest { venues ->
                    venues.forEach {
                        if (it.lat != 0.0)
                            addMarker(
                                lat = it.lat,
                                lng = it.lng,
                                title = it.title,
                                hue = BitmapDescriptorFactory.HUE_AZURE,
                                placeId = it.venueId,
                                placeType = "venue"
                            )
                        }
                    }

            viewModel.eventsState
                .filterNotNull()
                .collectLatest { events ->
                    events.forEach {
                        if (it.lat != 0.0) {
                            addMarker(
                                lat = it.lat,
                                lng = it.lng,
                                title = it.name,
                                hue = BitmapDescriptorFactory.HUE_RED,
                                placeId = it.eventId,
                                placeType = "event"
                            )
                        }
                    }
                }
        }
        lifecycleScope.launch {
            sharedViewModel.sharedRouteDestinationCoordinates
                .debounce(800)
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest { destinationCoordinates ->
                    val marker = viewModel.findMarkerByLatLng(
                        destinationCoordinates.latitude,
                        destinationCoordinates.longitude
                    )
                    Log.e("Markerer",marker.toString())
                    if(marker!=null){
                        marker.showInfoWindow()
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(marker.position.latitude, marker.position.longitude), 15f))
                    }
                }
        }
        googleMap.setOnMarkerClickListener { marker ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(marker.position.latitude-0.005, marker.position.longitude), 15f))
            marker.showInfoWindow()
            val tags = marker.tag as List<*>
            val placeId = tags[0] as Int
            val placeType = tags[1] as String
            placeId.let {
                findNavController().navigate(MapFragmentDirections.actionMapFragmentToMarkerDetailsBottomSheet(
                    placeId,
                    placeType,
                    marker.position.latitude.toString(),
                    marker.position.longitude.toString())
                )
            }
            true
        }

        googleMap.uiSettings.setAllGesturesEnabled(true)
        getMyLocation()
        applyDynamicMapStyle()
    }

    private fun applyDynamicMapStyle() {
        val themePreference = viewModel.sharedPrefTheme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val styleResId = when (themePreference) {
            AppCompatDelegate.MODE_NIGHT_YES -> R.raw.map_aubergine
            AppCompatDelegate.MODE_NIGHT_NO -> R.raw.map_daylight
            else -> {
                val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES)
                    R.raw.map_aubergine
                else
                    R.raw.map_daylight
            }
        }
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), styleResId)
            )
            if (!success)
                Log.e("map","Map style parsing failed.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("map","Map style parsing failed.")
        }
    }
}



//    private fun setDrawer() {
//
//        binding.buttonDrawer.setOnClickListener {
//            binding.myDrawerLayout.openDrawer(Gravity.LEFT, true)
//        }
//
//        val drawerLayout = binding.myDrawerLayout
//        val navigationView: NavigationView = requireActivity().findViewById(R.id.drawerNavigationHome)
//
//        val username = "username temp" //shprefLoggedin.getString("username", null)
//        val useremail = "email temp" //shprefLoggedin.getString("email", null)
//
//        val headerView = navigationView.getHeaderView(0)
//        val userProfilePicture = headerView.findViewById<ShapeableImageView>(R.id.imageUserProfilePictureDrawer)
//        val usernameDrawer = headerView.findViewById<TextView>(R.id.textUsernameDrawer)
//        val userEmailDrawer = headerView.findViewById<TextView>(R.id.textUserEmailDrawer)
//        val editProfileDrawer = headerView.findViewById<Button>(R.id.buttonEditProfileDrawer)
//
//
//        usernameDrawer.text = username
//        userEmailDrawer.text = useremail
//
//        Glide.with(userProfilePicture)
//            .load(getProfilePictureUri())
//            .placeholder(R.drawable.usersample)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .into(userProfilePicture)
//
//        editProfileDrawer.setOnClickListener {
//        nancyToastSuccess(requireContext(), getString(R.string.edit))
//            //TODO -> edit screen
//        }
//
//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.drawer_profile -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    findNavController().navigate(R.id.profileFragment)
//                    true
//                }
//                R.id.drawer_referral -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    findNavController().navigate(R.id.referralFragment)
//                    true
//                }
//                R.id.drawer_subscription -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    findNavController().navigate(R.id.subscriptionFragment)
//                    true
//                }
//                R.id.drawer_tickets -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    //findNavController().navigate(R.id.)
//                    //TODO -> current tickets
//                    true
//                }
//                R.id.drawer_payment_history -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    //findNavController().navigate(R.id.)
//                    //TODO -> all previous payments and active tickets
//                    true
//                }
//                R.id.drawer_settings -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    //findNavController().navigate(R.id.)
//                    //TODO -> profile info and edit screen
//                    true
//                }
//                R.id.drawer_logout -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    sharedPreferences.edit{
//                        clear()
//                    }
//                    nancyToastInfo(requireContext(), getString(R.string.logout_successful))
//                    navigateToAuthActivity()
//                    true
//                }
//                else -> false
//            }
//        }
//    }