package com.arifahmadalfian.rdsmapbox.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.DetailActivity
import com.arifahmadalfian.rdsmapbox.R
import com.arifahmadalfian.rdsmapbox.model.Pelanggan

class SearchAdapter(var pelanggan: List<Pelanggan>, var clickListener: IOnPelangganItemClickListener): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_row_pelanggan, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pelanggan.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        /*
        val img = pelanggan.photo
        val bm = BitmapFactory.decodeByteArray(img, 0, img.size)
        holder.nama.text = pelanggan[position].nama
        holder.alamatPemesan.text = pelanggan[position].alamat_pemesan
        holder.alamatDikirim.text = pelanggan[position].alamat_dikirim
        holder.longitude.text = pelanggan[position].longitude.toString()
        holder.latitude.text = pelanggan[position].latitude.toString()
        holder.keterangan.text = pelanggan[position].keterangan
       // holder.photo.text = pelanggan[position].photo.toString()
        holder.telepon.text = pelanggan[position].telepon

        //holder.bind(pelanggan[position])
         */
        holder.initializeButton(pelanggan[position],clickListener)
    }


    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*
        fun bind(pelanggan: Pelanggan) {
            with(itemView) {
                val img = pelanggan.photo
                val bm = BitmapFactory.decodeByteArray(img, 0, img.size)
                tv_item_nama.text = pelanggan.nama
                tv_item_alamat_pemesan.text = pelanggan.alamat_pemesan
                tv_item_alamat_dikirim.text = pelanggan.alamat_dikirim
                tv_item_latitude.text = pelanggan.latitude.toString()
                tv_item_longitude.text = pelanggan.longitude.toString()
                tv_item_keterangan.text = pelanggan.keterangan
                tv_item_photo.setImageBitmap(bm)
                tv_item_telepon.text = pelanggan.telepon
            }
        }
         */

        var nama: TextView = itemView.findViewById(R.id.tv_item_nama)
        var alamatPemesan: TextView = itemView.findViewById(R.id.tv_item_alamat_pemesan)
        var alamatDikirim: TextView = itemView.findViewById(R.id.tv_item_alamat_dikirim)
        var latitude: TextView = itemView.findViewById(R.id.tv_item_latitude)
        var longitude: TextView = itemView.findViewById(R.id.tv_item_longitude)
        var keterangan: TextView = itemView.findViewById(R.id.tv_item_keterangan)
        var telepon: TextView = itemView.findViewById(R.id.tv_item_telepon)
        var photo: ImageView = itemView.findViewById(R.id.tv_item_photo)

        var btnMapbox: Button = itemView.findViewById(R.id.btn_mapbox)
        var btnGoogleMaps: Button = itemView.findViewById(R.id.btn_googlemaps)


        fun initializeButton(pelanggan: Pelanggan, actions: IOnPelangganItemClickListener){
            val img = pelanggan.photo
            val bm = BitmapFactory.decodeByteArray(img, 0, img.size)
            nama.text = pelanggan.nama
            alamatPemesan.text = pelanggan.alamat_pemesan
            alamatDikirim.text = pelanggan.alamat_dikirim
            latitude.text = pelanggan.latitude.toString()
            longitude.text = pelanggan.longitude.toString()
            keterangan.text = pelanggan.keterangan
            telepon.text = pelanggan.telepon
            photo.setImageBitmap(bm)

            itemView.setOnClickListener {
                actions.onItemclick(pelanggan, adapterPosition)
            }
            btnMapbox.setOnClickListener {
                actions.onButtonMapboxClick(pelanggan, adapterPosition)
            }
            btnGoogleMaps.setOnClickListener {
                actions.onButtonGoogleMapsClick(pelanggan, adapterPosition)
            }
        }
    }
}

interface IOnPelangganItemClickListener{
    fun onItemclick(item: Pelanggan, position: Int)
    fun onButtonMapboxClick(item: Pelanggan, position: Int)
    fun onButtonGoogleMapsClick(item: Pelanggan, position: Int)
}

