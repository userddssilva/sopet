package br.edu.uea.spd.sopet.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.edu.uea.spd.sopet.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PlacesFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val sf = LatLng(-3.091435712412849, -60.01724962729166)
        googleMap.setMaxZoomPreference(17.0f);
        googleMap.setMinZoomPreference(12.0f);
        googleMap.addMarker(
            MarkerOptions()
            .position(sf)
            .icon(context?.let { BitmapFromVector(it, R.drawable.ic_baseline_purple_pets_24) })
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sf))
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 2000, null)
        googleMap.addMarker(
            MarkerOptions().position(LatLng(-3.1075044, -60.0713255))
                .icon(context?.let { BitmapFromVector(it, R.drawable.ic_baseline_purple_pets_24) })
        )

        googleMap.addMarker(
            MarkerOptions().position(LatLng(-3.127145,-60.039139))
                .icon(context?.let { BitmapFromVector(it, R.drawable.ic_baseline_purple_pets_24) })
        )

        googleMap.addMarker(
            MarkerOptions().position(LatLng(-3.127145,-60.0391391))
                .icon(context?.let { BitmapFromVector(it, R.drawable.ic_baseline_purple_pets_24) })
        )

        googleMap.addMarker(
            MarkerOptions().position(LatLng(-3.127145,-60.0391391))
                .icon(context?.let { BitmapFromVector(it, R.drawable.ic_baseline_red_pets_24) })
        )
        
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(br.edu.uea.spd.sopet.R.layout.fragment_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

//    fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34, 151)
//        mMap.addMarker(
//            MarkerOptions().position(sydney)
//                .title("Marker in Sydney") // below line is use to add custom marker on our map.
//                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_flag))
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}