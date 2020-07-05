package com.arifahmadalfian.rdsmapbox.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
import androidx.core.database.getBlobOrNull
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.*

class Database(context: Context?) : SQLiteAssetHelper(
    context,
    DB_NAME,
    null,
    DB_VER
) {
    val kontek: Context? = context


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
                val img: ByteArray = cursor.getBlob(7)
                //val bt: Bitmap = BitmapFactory.decodeByteArray(img,0,img.size)

                val pelanggan = Pelanggan(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nama = cursor.getString(cursor.getColumnIndex("nama")),
                    alamat_pemesan = cursor.getString(cursor.getColumnIndex("alamat_pemesan")),
                    alamat_dikirim = cursor.getString(cursor.getColumnIndex("alamat_dikirim")),
                    longitude = cursor.getDouble(cursor.getColumnIndex("longitudes")),
                    latitude = cursor.getDouble(cursor.getColumnIndex("latitudes")),
                    keterangan = cursor.getString(cursor.getColumnIndex("keterangan")),
                    photo = cursor.getBlob(7),
                    telepon = cursor.getString(cursor.getColumnIndex("telepon"))
                )
                result.add(pelanggan)
            } while (cursor.moveToNext())
        }
        return result
    }

    @SuppressLint("Recycle")
    fun getPelangganByAlamat(alamatDikirim: String): List<Pelanggan>?{
        val result: MutableList<Pelanggan> = ArrayList()
        val db = readableDatabase
        if (db != null) {
            val cursor = db.rawQuery(
                "SELECT * FROM Pelanggan WHERE alamat_dikirim LIKE '%$alamatDikirim%'",
                null
            )
            if(cursor.count != 0) {
                while (cursor.moveToNext()){
                    val id = cursor.getInt(0)
                    val nama = cursor.getString(1)
                    val alamat_pemesan= cursor.getString(2)
                    val alamat_dikirim = cursor.getString(3)
                    val longitude = cursor.getDouble(4)
                    val latitude = cursor.getDouble(5)
                    val keterangan = cursor.getString(6)
                    val poto = cursor.getBlob( 7)
                    val telepon = cursor.getString(8)
                    val pelanggan = Pelanggan(
                        id ,
                        nama ,
                        alamat_pemesan,
                        alamat_dikirim,
                        longitude,
                        latitude ,
                        keterangan,
                        poto,
                        telepon
                    )
                    result.add(pelanggan)
                }
            } else {
                Toast.makeText(kontek, "Data tidak ada", Toast.LENGTH_SHORT).show()
                return null
            }
        } else {
            Toast.makeText(kontek, "Data tidak ada", Toast.LENGTH_SHORT).show()
            return null
        }
        return result
    }

    companion object {
        private const val DB_NAME = "rds.db"
        private const val DB_VER = 1
    }
}