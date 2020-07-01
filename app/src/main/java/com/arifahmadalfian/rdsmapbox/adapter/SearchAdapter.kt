package com.arifahmadalfian.rdsmapbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.R
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import kotlinx.android.synthetic.main.item_row_pelanggan.view.*
import kotlinx.android.synthetic.main.layout_alamat.view.*

class SearchAdapter(var pelanggan: List<Pelanggan>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*
        var nama: TextView = itemView.findViewById(R.id.tv_item_nama)
        var alamatPemesan: TextView = itemView.findViewById(R.id.tv_item_alamat_pemesan)
        var alamatDikirim: TextView = itemView.findViewById(R.id.tv_item_alamat_dikirim)
        var latitude: TextView = itemView.findViewById(R.id.tv_item_latitude)
        var longitude: TextView = itemView.findViewById(R.id.tv_item_longitude)
        var keterangan: TextView = itemView.findViewById(R.id.tv_item_keterangan)
        var telepon: TextView = itemView.findViewById(R.id.tv_item_telepon)
        var photo: ImageView = itemView.findViewById(R.id.tv_item_photo)

         */

        fun bind(pelanggan: Pelanggan) {
            with(itemView) {
/*
                Glide.with(itemView.context)
                    .load(pelanggan.photo)
                    .apply(RequestOptions().override(100,100))
                    .into(tv_item_photo)

 */
                tv_item_nama.text = pelanggan.nama
                tv_item_alamat_pemesan.text = pelanggan.alamat_pemesan
                tv_item_alamat_dikirim.text = pelanggan.alamat_dikirim
                tv_item_latitude.text = pelanggan.latitude.toString()
                tv_item_longitude.text = pelanggan.longitude.toString()
                tv_item_keterangan.text = pelanggan.keterangan
                tv_item_photo.setImageBitmap(pelanggan.photo)
                tv_item_telepon.text = pelanggan.telepon
            }
        }

    }

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
        holder.nama.text = pelanggan[position].nama
        holder.alamatPemesan.text = pelanggan[position].alamat_pemesan
        holder.alamatDikirim.text = pelanggan[position].alamat_dikirim
        holder.longitude.text = pelanggan[position].longitude.toString()
        holder.latitude.text = pelanggan[position].latitude.toString()
        holder.keterangan.text = pelanggan[position].keterangan
       // holder.photo.text = pelanggan[position].photo.toString()
        holder.telepon.text = pelanggan[position].telepon

         */

        holder.bind(pelanggan[position])
    }
}