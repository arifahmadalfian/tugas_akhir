package com.arifahmadalfian.rdsmapbox.database

import android.content.Context
import android.database.sqlite.SQLiteQueryBuilder
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.util.*

class Database(context: Context?) : SQLiteAssetHelper(
    context,
    DB_NAME,
    null,
    DB_VER
) {

    //fungsi untuk memanggil data pelanggan
    val pelanggan: List<Pelanggan>
        get() {
            val db = readableDatabase
            val qb = SQLiteQueryBuilder()

            //menambahkan nama tabel dan nama nama kolom
            val sqlSelect = arrayOf(
                "id",
                "nama",
                "alamat_pemesan",
                "alamat_dikirim",
                "latitude",
                "longitude",
                "keterangan",
                "photo",
                "telepon"
            )
            val tableName = "Pelanggan" //nama tabelnya
            qb.tables = tableName
            val cursor = qb.query(db, sqlSelect, null, null, null, null, null)
            val result: MutableList<Pelanggan> = ArrayList()
            if (cursor.moveToFirst()) {
                do {
                    val pelanggan = Pelanggan(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nama = cursor.getString(cursor.getColumnIndex("nama")),
                    alamat_pemesan = cursor.getString(cursor.getColumnIndex("alamat_pemesan")),
                    alamat_dikirim = cursor.getString(cursor.getColumnIndex("alamat_dikirim")),
                    latitude = cursor.getDouble(cursor.getColumnIndex("latitude")),
                    longitude = cursor.getDouble(cursor.getColumnIndex("longitude")),
                    keterangan = cursor.getString(cursor.getColumnIndex("keterangan")),
                    photo = cursor.getInt(cursor.getColumnIndex("photo")),
                    telepon = cursor.getString(cursor.getColumnIndex("telepon"))
                    )
                    result.add(pelanggan)
                } while (cursor.moveToNext())
            }
            return result
        }

    //fungsi untuk memanggil nama pelanggan
    val nama: List<String>
        get() {
            val db = readableDatabase
            val qb = SQLiteQueryBuilder()

            //menambahkan nama tabel dan nama nama kolom
            val sqlSelect = arrayOf(
                "nama"
            )
            val tableName = "Pelanggan" //nama tabelnya
            qb.tables = tableName
            val cursor = qb.query(db, sqlSelect, null, null, null, null, null)
            val result: MutableList<String> = ArrayList()
            if (cursor.moveToFirst()) {
                do {
                    result.add(cursor.getString(cursor.getColumnIndex("nama")))
                } while (cursor.moveToNext())
            }
            return result
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
            "latitude",
            "longitude",
            "keterangan",
            "photo",
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
                val pelanggan = Pelanggan(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nama = cursor.getString(cursor.getColumnIndex("nama")),
                    alamat_pemesan = cursor.getString(cursor.getColumnIndex("alamat_pemesan")),
                    alamat_dikirim = cursor.getString(cursor.getColumnIndex("alamat_dikirim")),
                    latitude = cursor.getDouble(cursor.getColumnIndex("latitude")),
                    longitude = cursor.getDouble(cursor.getColumnIndex("longitude")),
                    keterangan = cursor.getString(cursor.getColumnIndex("keterangan")),
                    photo = cursor.getInt(cursor.getColumnIndex("photo")),
                    telepon = cursor.getString(cursor.getColumnIndex("telepon"))
                )
                result.add(pelanggan)
            } while (cursor.moveToNext())
        }
        return result
    }
    fun getPelangganByName(nama: String): List<Pelanggan>? {
        val db = readableDatabase
        val qb = SQLiteQueryBuilder()

        //menambahkan nama tabel dan nama nama kolom
        val sqlSelect = arrayOf(
            "id",
            "nama",
            "alamat_pemesan",
            "alamat_dikirim",
            "latitude",
            "longitude",
            "keterangan",
            "photo",
            "telepon"
        )
        val tableName = "Pelanggan" //nama tabelnya
        qb.tables = tableName
        val cursor = qb.query(
            db,
            sqlSelect,
            "nama LIKE ?",
            arrayOf("%$nama%"),
            null,
            null,
            null
        )
        val result: MutableList<Pelanggan> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val pelanggan = Pelanggan(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    nama = cursor.getString(cursor.getColumnIndex("nama")),
                    alamat_pemesan = cursor.getString(cursor.getColumnIndex("alamat_pemesan")),
                    alamat_dikirim = cursor.getString(cursor.getColumnIndex("alamat_dikirim")),
                    latitude = cursor.getDouble(cursor.getColumnIndex("latitude")),
                    longitude = cursor.getDouble(cursor.getColumnIndex("longitude")),
                    keterangan = cursor.getString(cursor.getColumnIndex("keterangan")),
                    photo = cursor.getInt(cursor.getColumnIndex("photo")),
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