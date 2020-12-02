package com.arifahmadalfian.rdsmapbox.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Admin(
    var nama: String? = null,
    var email: String? = null,
    var password: String? = null,
    var telepon: String? = null,
    var alamat: String? = null,
    var photo: String? = null): Parcelable