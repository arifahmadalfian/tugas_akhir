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
        var nama: TextView = itemView.findViewById(R.id.tv_item_nama)
        var alamat: TextView = itemView.findViewById(R.id.tv_item_alamat)
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
        holder.nama.text = pelanggan[position].nama
        holder.alamat.text = pelanggan[position].alamat_dikirim
    }
}