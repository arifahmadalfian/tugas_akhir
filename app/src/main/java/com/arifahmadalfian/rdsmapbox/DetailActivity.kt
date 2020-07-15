package com.arifahmadalfian.rdsmapbox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.adapter.SearchAdapter
import com.arifahmadalfian.rdsmapbox.database.Database

class DetailActivity : AppCompatActivity() {

    companion object {
        var Extra_pelanggan = "extra_pelanggan"
    }

    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: SearchAdapter? = null

    var database: Database? = null

    var koordinat: String? = null
    var destLat: String? = null
    var destLng: String? = null


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

        destLat = "-6.936855"
        destLng = "107.575232"
        koordinat = "$destLat,$destLng"

        //ujicoba mengirim data intent
        Toast.makeText(this@DetailActivity, "$koordinat", Toast.LENGTH_SHORT).show()

    }


    //onClick dari xml layout detail_activity
    fun getMapNavigation(view: View){
        when(view.id){
            R.id.btn_mapbox -> {
                val mDestlat = destLat
                val mDestLng = destLng
                getMapbox(mDestlat, mDestLng)
            }
            R.id.btn_googlemaps -> {
                val gDestination = koordinat
                getGoogleMaps(gDestination)
            }
        }
    }

    private fun getMapbox(mDestlat: String?, mDestLng: String?) {
        try {
            val intent = Intent(this@DetailActivity,MainActivity::class.java)
            intent.putExtra(MainActivity.LATITUDES,mDestlat)
            intent.putExtra(MainActivity.LONGITUDES,mDestLng)
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