package com.arifahmadalfian.rdsmapbox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.adapter.IOnPelangganItemClickListener
import com.arifahmadalfian.rdsmapbox.adapter.SearchAdapter
import com.arifahmadalfian.rdsmapbox.database.Database
import com.arifahmadalfian.rdsmapbox.model.Pelanggan

class DetailActivity : AppCompatActivity(), IOnPelangganItemClickListener {

    companion object {
        var Extra_pelanggan = "extra_pelanggan"
    }

    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: SearchAdapter? = null

    var database: Database? = null

    private var koordinat: String? = null
    private var destLat: String? = null
    private var destLng: String? = null

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
        adapter = pelanggan?.let { database?.getPelangganByAlamat(it)?.let { SearchAdapter(it,this) } }
        recyclerView?.adapter = adapter

    }

    override fun onItemclick(item: Pelanggan, position: Int) {
        Toast.makeText(this, item.nama, Toast.LENGTH_SHORT).show()
    }

    override fun onButtonMapboxClick(item: Pelanggan, position: Int) {
        //mendapatkan data koordinat dari onitemclick button in recycler
        destLat = item.latitude.toString()
        destLng = item.longitude.toString()
        koordinat = "$destLat, $destLng"

        //button mapbox
        val mDestlat = destLat
        val mDestLng = destLng
        getMapbox(mDestlat, mDestLng)
    }

    override fun onButtonGoogleMapsClick(item: Pelanggan, position: Int) {
        //mendapatkan data koordinat dari onitemclick button in recycler
        destLat = item.latitude.toString()
        destLng = item.longitude.toString()
        koordinat = "$destLat, $destLng"

        //button google maps
        val gDestination = koordinat
        getGoogleMaps(gDestination)
    }

    private fun getMapbox(mDestlat: String?, mDestLng: String?) {
        try {
            val intent = Intent(this@DetailActivity,MapboxActivity::class.java)
            intent.putExtra(MapboxActivity.LATITUDES,mDestlat)
            intent.putExtra(MapboxActivity.LONGITUDES,mDestLng)
            startActivity(intent)
        } catch (e: Exception){
            Toast.makeText(this@DetailActivity,"Gagal Membuka Mapbox", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGoogleMaps(gDestination: String?) {
        try {
            val uri = Uri.parse("https://www.google.co.in/maps/dir/?api=1&destination=$gDestination")
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