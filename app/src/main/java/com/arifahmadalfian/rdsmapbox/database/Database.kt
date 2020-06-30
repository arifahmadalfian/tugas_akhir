package com.arifahmadalfian.rdsmapbox.database

import android.content.Context
import android.database.sqlite.SQLiteQueryBuilder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.*

class Database(context: Context?) : SQLiteAssetHelper(
    context,
    DB_NAME,
    null,
    DB_VER
) {
    val alamatdikirim: List<String>
        get() {
            val db = readableDatabase
            val qb = SQLiteQueryBuilder()

            //menambahkan nama tabel dan nama nama kolom
            val sqlSelect = arrayOf(
                "alamat_dikirim"
            )
            val tableName = "Pelanggan" //nama tabelnya
            qb.tables = tableName
            val cursor = qb.query(db, sqlSelect, null, null, null, null, null)
            val result: MutableList<String> = ArrayList()
            if (cursor.moveToFirst()) {
                do {
                    result.add(cursor.getString(cursor.getColumnIndex("alamat_dikirim")))
                } while (cursor.moveToNext())
            }
            return result
        }

    //filter pencarian nama pelanggan
    fun getPelangganByAlamatDikirim(alamatDikirim: String): List<Pelanggan>? {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        //menambahkan nama tabel dan nama nama kolom
        val sqlSelect = arrayOf(
            "id",
            "nama",
            "alamat_pemesan",
            "alamat_dikirim",
            "longitudes",
            "latitudes",
            "keterangan",
            "poto",
            "telepon"
        )
        val tableName = "Pelanggan" //nama tabelnya
        qb.tables = tableName
        val cursor = qb.query(
            db,
            sqlSelect,
            "alamat_dikirim LIKE ?",
            arrayOf("%$alamatDikirim%"),
            null,
            null,
            null
        )
        val result: MutableList<Pelanggan> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                var img = cursor.getBlob(cursor.getColumnIndex("poto"))
                var bt: Bitmap = BitmapFactory.decodeByteArray(img, 0, img.size-0)
                val pelanggan = Pelanggan(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nama = cursor.getString(cursor.getColumnIndex("nama")),
                    alamat_pemesan = cursor.getString(cursor.getColumnIndex("alamat_pemesan")),
                    alamat_dikirim = cursor.getString(cursor.getColumnIndex("alamat_dikirim")),
                    longitude = cursor.getDouble(cursor.getColumnIndex("longitudes")),
                    latitude = cursor.getDouble(cursor.getColumnIndex("latitudes")),
                    keterangan = cursor.getString(cursor.getColumnIndex("keterangan")),
                    photo = bt,
                    telepon = cursor.getString(cursor.getColumnIndex("telepon"))
                )

                result.add(pelanggan)
            } while (cursor.moveToNext())
        }
        return result
    }


    companion object {
        private const val DB_NAME = "rds.db"
        private const val DB_VER = 1
    }
}