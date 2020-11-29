package com.arifahmadalfian.rdsmapbox.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Pengirim(
    var nama: String? = null,
    var email_pengirim: String? = null,
    var password_pengirim: String? = null,
    var telepon: String? = null,
    var alamat: String? = null,
    var photo: String? = null): Parcelable