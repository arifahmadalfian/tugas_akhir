@file:Suppress("DEPRECATION")

package com.arifahmadalfian.rdsmapbox

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.adapter.SearchAdapter
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

    private var mMap: GoogleMap? = null
    var mGoogleApiClient: GoogleApiClient? = null
    private val mLocationRequest: LocationRequest? = null
    private val mCurrLocationMarker: Marker? = null
    private var mLastLocation: Location? = null


    var recyclerView: RecyclerView? = null
    var layoutManager: LinearLayoutManager? = null
    var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //cek user sudah pernah masuk atau belum
        //check kondisi jika sudah login langsung ke home
        //jika belum login akan di arahkan ke activiy login
        checkUserAccountSignIn()

    }

    private fun checkUserAccountSignIn() {
        if (getInstance().uid.isNullOrEmpty()) {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            Toast.makeText(this@HomeActivity, "Silahkan Login", Toast.LENGTH_SHORT).show()
        } else {
            // Memuat SupportMapFragment dan memberi notifikasi saat telah siap.
            val mapFragment =
                supportFragmentManager
                    .findFragmentById(R.id.maps) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)

            recyclerView = findViewById(R.id.list_pelanggan)
            layoutManager = LinearLayoutManager(this)
            recyclerView?.layoutManager = layoutManager

            val options: FirebaseRecyclerOptions<Pelanggan> =
                FirebaseRecyclerOptions.Builder<Pelanggan>()
                    .setQuery(
                        FirebaseDatabase.getInstance().reference.child("pelanggan").limitToFirst(10),
                        Pelanggan::class.java
                    )
                    .build()

            adapter = SearchAdapter(options)
            recyclerView?.adapter = adapter

        }
    }

    private fun startSearch(text: String) {
        val options: FirebaseRecyclerOptions<Pelanggan> =
            FirebaseRecyclerOptions.Builder<Pelanggan>().setQuery(
                FirebaseDatabase.getInstance()
                    .reference
                    .child("pelanggan")
                    .orderByChild("alamat_dikirim")
                    .startAt(text)
                    .endAt("$text\uf8ff")
                    .limitToFirst(10), Pelanggan::class.java
            ).build()

        tv_null.visibility = View.VISIBLE
        adapter = SearchAdapter(options)
        adapter?.startListening()
        recyclerView?.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.cari)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    list_pelanggan.visibility = View.VISIBLE
                    maps.view?.visibility = View.GONE
                    startSearch(newText)
                } else {
                    list_pelanggan.visibility = View.GONE
                    maps.view?.visibility = View.VISIBLE
                    tv_null.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                tv_null.visibility = View.VISIBLE
                Toast.makeText(this@HomeActivity, query, Toast.LENGTH_SHORT).show()
                return false
            }
        })

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {

                true
            }
            R.id.action_logout -> {
                getActionLogout()
                true
            }
            R.id.action_about -> {
                Toast.makeText(this@HomeActivity, " Tentang Aplikasi", Toast.LENGTH_SHORT).show()
                getActionAbout()
                true
            }
            else -> {

                true
            }
        }
    }

    private fun getActionLogout() {
        getInstance().signOut()
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Toast.makeText(this@HomeActivity, " Berhasil Logout", Toast.LENGTH_SHORT).show()
    }

    private fun getActionAbout() {
        val intent = Intent(this@HomeActivity, AboutActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Memulai Google Play Services
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            buildGoogleApiClient()
            mMap?.isMyLocationEnabled = true
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        mGoogleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            @Suppress("DEPRECATION")
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {  }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {  }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        mCurrLocationMarker?.remove()
        val latLng =
            LatLng(
                location.latitude,
                location.longitude
            )
        val cameraPosition =
            CameraPosition.Builder()
                .target(LatLng(latLng.latitude, latLng.longitude))
                .zoom(17f).build()
        mMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                cameraPosition
            )
        )

        Toast.makeText(this@HomeActivity, "Cari Toko Pelanggan", Toast.LENGTH_SHORT).show()

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            @Suppress("DEPRECATION")
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap?.isMyLocationEnabled = true
                    }
                } else {

                    // Izin ditolak.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

}