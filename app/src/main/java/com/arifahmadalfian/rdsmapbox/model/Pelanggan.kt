package com.arifahmadalfian.rdsmapbox.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Pelanggan(
    var nama: String? = null,
    var alamat_pemesan: String? = null,
    var alamat_dikirim: String? = null,
    var koordinat: String? = null,
    var keterangan: String? = null,
    var photo: String? = null,
    var telepon: String? = null): Parcelable
