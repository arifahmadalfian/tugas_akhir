package com.arifahmadalfian.rdsmapbox.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pelanggan(
    var id: Int,
    var nama: String,
    var alamat_pemesan: String,
    var alamat_dikirim: String,
    var longitude: Double,
    var latitude: Double,
    var keterangan: String,
    var photo: ByteArray,
    var telepon: String
) : Parcelable {}