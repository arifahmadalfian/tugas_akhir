package com.arifahmadalfian.rdsmapbox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.adapter.SearchAdapter
import com.arifahmadalfian.rdsmapbox.database.Database
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import kotlinx.android.synthetic.main.item_row_pelanggan.*
import java.util.*

class DetailActivity : AppCompatActivity() {

    companion object {
        var Extra_pelanggan = "extra_pelanggan"
    }

    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: SearchAdapter? = null

    var database: Database? = null

    var lokasiUser: Geocoder? = null
    var longitudes: TextView? = null
    var latitudes: TextView? = null
    var koordinat: String? = null

    var lng: Double? = -6.949504
    var lat: Double? = 107.585609

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        recyclerView = findViewById(R.id.rv_pelanggan)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        database = Database(this)

        val pelanggan = intent.getStringExtra(Extra_pelanggan)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        adapter = database?.getPelangganByAlamat(pelanggan)?.let { SearchAdapter(it) }
        recyclerView?.adapter = adapter

        lokasiUser = Geocoder(this, Locale.getDefault())
        longitudes = findViewById(R.id.tv_item_longitude)
        latitudes = findViewById(R.id.tv_item_latitude)
        //koordinat = "$longitudes,$latitudes"
        koordinat = "$lng,$lat"
    }


    fun getMapNavigation(view: View){
        when(view.id){
            R.id.btn_mapbox -> {

            }
            R.id.btn_googlemaps -> {
                val sLokasiUser = lokasiUser.toString().trim()
                val sKoordinat = koordinat.toString().trim()
                getGoogleMaps(sLokasiUser,sKoordinat)

            }
        }
    }

    private fun getGoogleMaps(sLokasiUser: String, sKoordinat: String) {
        try {
            val uri = Uri.parse("https://www.google.co.in/maps/dir/$sLokasiUser/$sKoordinat")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: ActivityNotFoundException){
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
            val intent = Intent(ACTION_VIEW, uri)
            startActivity(intent)
        }
    }


}