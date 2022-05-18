package com.google.firebase.quickstart.auth.outdoorexplorer.ui.map

import com.google.firebase.quickstart.auth.outdoorexplorer.R
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MapFragment : Fragment() {
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapViewModel = ViewModelProvider(this)
            .get(MapViewModel::class.java)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { map ->
            googleMap = map
            val bay = LatLng(37.68, -122.42)
            map.moveCamera(CameraUpdateFactory.zoomTo(10f))
            map.moveCamera(CameraUpdateFactory.newLatLng(bay))
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isTiltGesturesEnabled = false

            mapViewModel.allLocations.observe(viewLifecycleOwner, Observer {
                for (location in it) {
                    val point = LatLng(location.latitude, location.longitude)
                    val marker = map.addMarker(
                        MarkerOptions()
                            .position(point)
                            .title(location.title)
                            .snippet("Hours: ${location.hours}")
                            .icon(
                                getBitmapFromVector(
                                    R.drawable.ic_baseline_star_24,
                                    R.color.colorAccent
                                )
                            )
                            .alpha(0.75f)
                    )
                    marker?.tag = location.locationId

                    map.addCircle(
                        CircleOptions().center(point).radius(location.geofenceRadius.toDouble())
                    )
                }
            })

            map.setOnInfoWindowClickListener { marker ->
                val action = MapFragmentDirections.actionNavigationMapToNavigationLocation()
                action.locationId = marker.tag as Int
                val navController = Navigation.findNavController(requireView())
                navController.navigate(action)
            }

            enableMyLocation()
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
    private fun enableMyLocation() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.map_snackbar),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.map_rationale),
                        RC_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
                .show()
        }
    }

    private fun getBitmapFromVector(
        @DrawableRes vectorResourceId: Int,
        @ColorRes colorResourceId: Int
    ): BitmapDescriptor {
        val vectorDrawable = resources.getDrawable(vectorResourceId, requireContext().theme)
            ?: return BitmapDescriptorFactory.defaultMarker()

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(
            vectorDrawable,
            ResourcesCompat.getColor(
                resources,
                colorResourceId, requireContext().theme
            )
        )
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        const val RC_LOCATION = 10
    }
}
