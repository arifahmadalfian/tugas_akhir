package com.arifahmadalfian.rdsmapbox.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pelanggan(
    var id: Int,
    var nama: String,
    var alamat_pemesan: String,
    var alamat_dikirim: String,
    var latitude: Double,
    var longitude: Double,
    var keterangan: String,
    var photo: ByteArray,
    var telepon: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pelanggan

        if (id != other.id) return false
        if (nama != other.nama) return false
        if (alamat_pemesan != other.alamat_pemesan) return false
        if (alamat_dikirim != other.alamat_dikirim) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (keterangan != other.keterangan) return false
        if (!photo.contentEquals(other.photo)) return false
        if (telepon != other.telepon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + nama.hashCode()
        result = 31 * result + alamat_pemesan.hashCode()
        result = 31 * result + alamat_dikirim.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + keterangan.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + telepon.hashCode()
        return result
    }

}