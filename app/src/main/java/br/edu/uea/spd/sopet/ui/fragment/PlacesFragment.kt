package br.edu.uea.spd.sopet.ui.fragment

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.uea.spd.sopet.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class PlacesFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        // Add a marker in Sydney and move the camera
        val sf = LatLng(-3.091435712412849, -60.01724962729166)
        googleMap.setMaxZoomPreference(17.0f);
        googleMap.setMinZoomPreference(12.0f);
        googleMap.addMarker(MarkerOptions()
            .position(sf)
            .title("Manaus"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sf))
        googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 2000, null)
        
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}