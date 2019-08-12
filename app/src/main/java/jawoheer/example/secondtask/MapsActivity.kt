package jawoheer.example.secondtask

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var geo: Geocoder
    private lateinit var addresss: List<Address>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val usa=LatLng(36.938287, -120.128919)
        val marker=MarkerOptions().position(usa).draggable(true).title("Marker in USA")
        mMap.addMarker(marker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_eta_main))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(usa))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.2f), 400, null)

        val polygon=drawRouteOnMap(mMap)

        val bounds: LatLngBounds
        val builder = LatLngBounds.Builder()

        builder.include(polygon.points[0])
        builder.include(polygon.points[1])
        builder.include(polygon.points[2])
        builder.include(polygon.points[3])
        builder.include(polygon.points[4])
        bounds=builder.build()

        mMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragEnd(p0: Marker?) {

            }

            override fun onMarkerDragStart(p0: Marker?) {

            }

            override fun onMarkerDrag(marker: Marker) {

                if(bounds.contains(marker.position)){
                    region_layout.visibility= VISIBLE
                    region_layout1.visibility= GONE
                    background_scrim.visibility= GONE

                    geo= Geocoder(this@MapsActivity, Locale.ENGLISH)
                    addresss=geo.getFromLocation(marker.position.latitude, marker.position.longitude,1)
                    if(addresss.isNotEmpty()) region_txt.text=addresss.get(0).getAddressLine(0)

                }
                else{
                    region_layout.visibility= GONE
                    region_layout1.visibility= VISIBLE
                    background_scrim.visibility= VISIBLE
                }
            }
        })
    }


    private fun drawRouteOnMap(map : GoogleMap):Polygon {

        val rectOptions = PolygonOptions()
            .add(
//                LatLng(41.274613, 69.204815),
//                LatLng(41.292264, 69.223340),
//                LatLng(41.277901, 69.245220),
//                LatLng(41.260912, 69.226110),
//                LatLng(41.274613, 69.204815)
                LatLng(36.974213, -120.128267),
                LatLng(36.938287, -120.128919),
                LatLng(36.937999, -120.019708),
                LatLng(36.973927, -120.020006),
                LatLng(36.974213, -120.128267)

            )

        val polygon = map.addPolygon(rectOptions)
        return polygon
    }
}
