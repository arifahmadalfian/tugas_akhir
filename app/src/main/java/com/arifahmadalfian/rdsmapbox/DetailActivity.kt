package com.arifahmadalfian.rdsmapbox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.adapter.SearchAdapter
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        var Extra_pelanggan = "extra_pelanggan"
    }

    var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: SearchAdapter? = null

    private var koordinat: String? = null
    private var destLat: String? = null
    private var destLng: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val pelanggan: Pelanggan? = intent.getParcelableExtra(Extra_pelanggan)

        Glide.with(this)
            .load(pelanggan?.photo)
            .apply(RequestOptions().override(300, 250))
            .into(tv_item_photo)
        tv_item_nama.text = pelanggan?.nama
        tv_item_alamat_pemesan.text = pelanggan?.alamat_pemesan
        tv_item_alamat_dikirim.text = pelanggan?.alamat_dikirim
        tv_item_keterangan.text = pelanggan?.keterangan
        tv_item_koordinat.text = pelanggan?.koordinat
        tv_item_telepon.text = pelanggan?.telepon

        btn_googlemaps.setOnClickListener {
            getGoogleMaps(tv_item_koordinat.text.toString())
        }

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