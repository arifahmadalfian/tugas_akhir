package com.arifahmadalfian.rdsmapbox.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteQueryBuilder
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
    val kontek: Context? = context

    companion object {
        private const val DB_NAME = "rds.db"
        private const val DB_VER = 1
    }

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
                    val pelanggan = Pelanggan(
                        id = cursor.getInt(0),
                        nama = cursor.getString(1),
                        alamat_pemesan = cursor.getString(2),
                        alamat_dikirim = cursor.getString(3),
                        latitude = cursor.getDouble(4),
                        longitude = cursor.getDouble(5),
                        keterangan = cursor.getString(6),
                        photo = cursor.getBlob(7),
                        telepon = cursor.getString(8)
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

}