package com.arifahmadalfian.rdsmapbox

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.adapter.SearchAdapter
import com.arifahmadalfian.rdsmapbox.database.Database
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMap.OnMapClickListener
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.mapbox_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// classes needed to initialize map
// classes needed to add the location component
// classes needed to add a marker
// classes to calculate a route
// classes needed to launch navigation UI




class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener, OnMapClickListener,
    PopupMenu.OnMenuItemClickListener {
    // variables for adding location layer
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null

    // variables for adding location layer
    private var permissionsManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null

    // variables for calculating and drawing a route
    private var currentRoute: DirectionsRoute? = null
    private var navigationMapRoute: NavigationMapRoute? = null

    // variables needed to initialize navigation
    private var button: Button? = null

    // variabel untuk mode menu
    private var mode: Int = 0

    //variabel untuk menu
    private var menuToolbar: MenuItem? = null

    companion object {
        const val TAG = "DirectionsActivity"
        var longitudes: Double = -6.949504
        var latitudes: Double = 107.585609

    }

    //extension properties untuk mengubah variabel dari Point menjadi variabel local
    private val LatLng.lat: Double
        get() = latitudes

    private val LatLng.lng: Double
        get() = longitudes

    var searchBar: MaterialSearchBar? = null

    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: SearchAdapter? = null

    var suggestList: List<String> = ArrayList()

    var database: Database? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access))
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        recyclerView = findViewById(R.id.rv_pelanggan)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        getAppBarSearch()

    }

    private fun getAppBarSearch() {
        searchBar = findViewById(R.id.appbar_search)

        database = Database(this)

        searchBar?.inflateMenu(R.menu.main_menu);
        searchBar?.menu?.setOnMenuItemClickListener(this)

        // saat pencarian di tekan maka sugesti dari nama pelanggan akan muncul
        //val suggestList = database?.nama
        //searchBar?.lastSuggestions = suggestList

        val suggestListAlamat = database?.alamatdikirim
        searchBar?.lastSuggestions = suggestListAlamat?.take(5)

        searchBar?.addTextChangeListener(object: TextWatcher{
            @SuppressLint("DefaultLocale")
            override fun afterTextChanged(s: Editable?) {
            }

            @SuppressLint("DefaultLocale")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("DefaultLocale")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val suggest: MutableList<String> = ArrayList()
                suggestListAlamat?.forEach { search ->
                    if(search.toLowerCase().contains(searchBar?.text?.toLowerCase().toString())){
                        suggest.add(search)
                    }
                }
                searchBar?.lastSuggestions = suggest

            }

        })

        searchBar?.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {
                TODO("Not yet implemented")
            }

            override fun onSearchStateChanged(enabled: Boolean) {
                if(!enabled) {
                    recyclerView?.adapter = adapter
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString())

            }

        })
    }

    private fun startSearch(text: String) {
        if (text.isNotEmpty()){
            val moveTextPelanggan = Intent(this@MainActivity, DetailActivity::class.java)
            moveTextPelanggan.putExtra(DetailActivity.Extra_pelanggan, text)
            startActivity(moveTextPelanggan)
        }
        else{
            Toast.makeText(this@MainActivity, "Pencarian tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            getString(R.string.navigation_guidance_day)
        ) { style ->
            enableLocationComponent(style)
            addDestinationIconSymbolLayer(style)
            mapboxMap.addOnMapClickListener(this@MainActivity)

            button = findViewById(R.id.startButton)
            button?.setOnClickListener(View.OnClickListener {
                val simulateRoute = true
                val options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build()
                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(this@MainActivity, options)
            })
        }
    }

    private fun addDestinationIconSymbolLayer(loadedMapStyle: Style) {
        loadedMapStyle.addImage(
            "destination-icon-id",
            BitmapFactory.decodeResource(this.resources, R.drawable.mapbox_marker_icon_default)
        )
        val geoJsonSource = GeoJsonSource("destination-source-id")
        loadedMapStyle.addSource(geoJsonSource)
        val destinationSymbolLayer =
            SymbolLayer("destination-symbol-layer-id", "destination-source-id")
        destinationSymbolLayer.withProperties(
            PropertyFactory.iconImage("destination-icon-id"),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true)
        )
        loadedMapStyle.addLayer(destinationSymbolLayer)
    }

    override fun onMapClick(point: LatLng): Boolean {
        val destinationPoint =
            Point.fromLngLat(point.lat ,point.lng )
        val originPoint = Point.fromLngLat(
            locationComponent!!.lastKnownLocation!!.longitude,
            locationComponent!!.lastKnownLocation!!.latitude
        )
        val source =
            mapboxMap!!.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
        source?.setGeoJson(Feature.fromGeometry(destinationPoint))
        getRoute(originPoint, destinationPoint)
        button!!.isEnabled = true
        button!!.setBackgroundResource(R.color.mapboxBlue)
        return true
    }

    private fun getRoute(
        origin: Point,
        destination: Point
    ) {
        NavigationRoute.builder(this)
            .accessToken(Mapbox.getAccessToken()!!)
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : Callback<DirectionsResponse?> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<DirectionsResponse?>,
                    response: Response<DirectionsResponse?>
                ) {
                    // You can get the generic HTTP info about the response
                    Log.d(
                        TAG,
                        "Response code: " + response.code()
                    )
                    if (response.body() == null) {
                        Log.e(
                            TAG,
                            "No routes found, make sure you set the right user and access token."
                        )
                        return
                    } else if (response.body()!!.routes().size < 1) {
                        Log.e(TAG, "No routes found")
                        return
                    }
                    currentRoute = response.body()!!.routes()[0]

                    // Draw the route on the map
                    if (navigationMapRoute != null) {
                        @Suppress("DEPRECATION")
                        navigationMapRoute!!.removeRoute()
                    } else {
                        navigationMapRoute =
                            NavigationMapRoute(
                                null,
                                mapView!!,
                                mapboxMap!!,
                                R.style.NavigationMapRoute
                            )
                    }
                    navigationMapRoute!!.addRoute(currentRoute)
                }

                @SuppressLint("LogNotTimber")
                override fun onFailure(
                    call: Call<DirectionsResponse?>,
                    throwable: Throwable
                ) {
                    Log.e(TAG, "Error: " + throwable.message)
                }
            })
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap?.locationComponent
            @Suppress("DEPRECATION")
            locationComponent?.activateLocationComponent(this, loadedMapStyle)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationComponent?.isLocationComponentEnabled = true
            // Set the component's camera mode
            locationComponent?.cameraMode = CameraMode.TRACKING
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager?.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap?.style!!)
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun getActionLogout() {
        //list_main.visibility = View.GONE
        mapbox_main.visibility = View.VISIBLE
    }

    private fun getActionAbout() {
       // list_main.visibility = View.VISIBLE
        mapbox_main.visibility = View.GONE
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_about -> {
                Toast.makeText(this@MainActivity, " Ini action about", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_logout -> {
                Toast.makeText(this@MainActivity, " Ini action logout", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> false
        }
    }


}







