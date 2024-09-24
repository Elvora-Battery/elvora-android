package com.unsoed.elvora.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.unsoed.elvora.R
import com.unsoed.elvora.data.ApiResult
import com.unsoed.elvora.data.MapRequest
import com.unsoed.elvora.data.response.map.StationsItem
import com.unsoed.elvora.databinding.ActivityMapsBinding
import com.unsoed.elvora.helper.HomeModelFactory
import com.unsoed.elvora.ui.home.HomeViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val boundsBuilder = LatLngBounds.Builder()
    private var latLng: LatLng? = null


    private val homeViewModel: HomeViewModel by viewModels {
        HomeModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLastLocation()
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun getRecommendation(latitude: Double, longitude: Double) {
        val mapRequest = MapRequest(
            location = com.unsoed.elvora.data.Location(latitude = latitude, longitude = longitude)
        )
        homeViewModel.getStationRecommendation(mapRequest).observe(this) {
            it?.let { response ->
                when(response) {
                    is ApiResult.Loading -> {
                        Toast.makeText(this, "Sync data...", Toast.LENGTH_LONG).show()
                    }

                    is ApiResult.Success -> {
                        val listStation: List<StationsItem>? = response.data.stations
                        setMarker(listStation)

                    }

                    is ApiResult.Error -> {
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }

                    else -> { }
                }
            }
        }
    }

    private fun setMarker(listStation: List<StationsItem>?) {
        listStation?.let {
            it.forEach { station ->
                val latLng = LatLng(station.latitude!!, station.longitude!!)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(station.station)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                )
                boundsBuilder.include(latLng)
            }

            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                100
            ))


            mMap.setOnInfoWindowClickListener { marker ->
                val idMarkerClick = marker.id
                val id = idMarkerClick.substring(1)

                val nameStation = listStation[id.toInt()].station
                val distanceStation = listStation[id.toInt()].distance

                if (nameStation != null) {
                    val modalBottomSheet  = MapDialogFragment()
                    modalBottomSheet.show(supportFragmentManager, MapDialogFragment.TAG)
                    modalBottomSheet.arguments = Bundle().apply {
                        putString(MapDialogFragment.NAME_STATION, nameStation)
                        putDouble(MapDialogFragment.DISTANCE_STATION, distanceStation ?: 0.0)
                    }
                }

                Log.d("InfoMarker:", id)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latLng = LatLng(location.latitude, location.longitude)
                    showStartMarker(location)
                    getRecommendation(location.latitude, location.longitude)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
    }
}