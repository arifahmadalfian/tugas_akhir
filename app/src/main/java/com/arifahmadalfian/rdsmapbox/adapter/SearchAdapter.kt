package com.arifahmadalfian.rdsmapbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.rdsmapbox.R
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(
    options: FirebaseRecyclerOptions<Pelanggan>
): FirebaseRecyclerAdapter<Pelanggan, SearchAdapter.SearchViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.items_pelanggan, parent, false)
        return SearchViewHolder(itemView)
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nama: TextView = itemView.findViewById(R.id.tv_nama)
        var alamatPemesan: TextView? = null
        var alamatDikirim: TextView = itemView.findViewById(R.id.tv_alamat_dikirim)
        var koordinat: TextView? = null
        var keterangan: TextView? = null
        var telepon: TextView? = null
        var photo: CircleImageView = itemView.findViewById(R.id.img_pelanggan)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int, model: Pelanggan) {
        holder.nama.text = model.nama
        holder.alamatPemesan?.text = model.alamat_pemesan
        holder.alamatDikirim.text = model.alamat_dikirim
        holder.koordinat?.text = model.koordinat
        holder.keterangan?.text = model.keterangan
        holder.telepon?.text = model.telepon
        holder.nama.text = model.nama
        Glide.with(holder.photo.context)
            .load(model.photo)
            .into(holder.photo)
    }

}

interface IOnPelangganItemClickListener{
    fun onItemclick(item: Pelanggan, position: Int)
    fun onButtonMapboxClick(item: Pelanggan, position: Int)
    fun onButtonGoogleMapsClick(item: Pelanggan, position: Int)
}

