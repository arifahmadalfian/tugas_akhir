package com.arifahmadalfian.rdsmapbox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.R
import com.arifahmadalfian.rdsmapbox.model.Pelanggan

class SearchAdapter(var context: Context, var pelanggan: List<Pelanggan>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {


    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var id: TextView = itemView.findViewById(R.id.tv_item_id)
        var nama: TextView = itemView.findViewById(R.id.tv_item_nama)
        var alamatPemesan: TextView = itemView.findViewById(R.id.tv_item_alamat_pemesan)
        var alamatDikirim: TextView = itemView.findViewById(R.id.tv_item_alamat_dikirim)
        var latitude: TextView = itemView.findViewById(R.id.tv_item_latitude)
        var longitude: TextView = itemView.findViewById(R.id.tv_item_longitude)
        var keterangan: TextView = itemView.findViewById(R.id.tv_item_keterangan)
        var photo: TextView = itemView.findViewById(R.id.tv_item_photo)
        var telepon: TextView = itemView.findViewById(R.id.tv_item_telepon)
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
        holder.id.text = pelanggan[position].id.toString()
        holder.nama.text = pelanggan[position].nama
        holder.alamatPemesan.text = pelanggan[position].alamat_pemesan
        holder.alamatDikirim.text = pelanggan[position].alamat_dikirim
        holder.longitude.text = pelanggan[position].longitude.toString()
        holder.latitude.text = pelanggan[position].latitude.toString()
        holder.keterangan.text = pelanggan[position].keterangan
        holder.photo.text = pelanggan[position].photo.toString()
        holder.telepon.text = pelanggan[position].telepon
    }
}