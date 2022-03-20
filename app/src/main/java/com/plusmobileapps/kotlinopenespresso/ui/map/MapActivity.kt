package com.plusmobileapps.kotlinopenespresso.ui.map

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.plusmobileapps.kotlinopenespresso.R
import com.plusmobileapps.kotlinopenespresso.databinding.MapDetailBottomSheetBinding
import com.plusmobileapps.kotlinopenespresso.databinding.MapFragmentBinding
import com.plusmobileapps.kotlinopenespresso.util.CountingIdlingResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: MapFragmentBinding
    private lateinit var bottomSheetBinding: MapDetailBottomSheetBinding

    @Inject
    lateinit var countingIdlingResource: CountingIdlingResource

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapFragmentBinding.inflate(layoutInflater)
        bottomSheetBinding = MapDetailBottomSheetBinding.bind(binding.mapDetailBottomSheet.root)
        setContentView(binding.root)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("andrew", "New state: $newState")
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    countingIdlingResource.increment()
                } else {
                    countingIdlingResource.decrement()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBinding.imageButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setOnMarkerClickListener { marker ->
            marker.title?.let {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBinding.mapMarkerDetailTextview.text = it
            }
            true // overriding listener prevents the info window from showing
        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(
            MarkerOptions()
            .position(sydney)
            .title(SYDNEY_MARKER_TITLE))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    companion object {
        const val SYDNEY_MARKER_TITLE = "Marker in Sydney"
    }
}