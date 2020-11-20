package com.arifahmadalfian.rdsmapbox

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.arifahmadalfian.rdsmapbox.model.PelangganNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_tambah_pelanggan.*
import java.util.*

class ActivityTambahPelanggan : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var PICK_PHOTO = 100
    private var PICK_CAMERA = 101
    private var PHOTO_URI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)

        // menambahkan circle loader/loading
        val tashie = TashieLoader(
            this, 5,
            30, 10,
            ContextCompat.getColor(this, R.color.colorLoginPrimaryDark))
            .apply {
                animDuration = 500
                animDelay = 50
                interpolator = LinearInterpolator()
            }
        tp_loading.addView(tashie)
        tp_loading.visibility = View.GONE

        initView()

    }

    private fun initView() {
        btn_tp_to_home.setOnClickListener {
            val intent = Intent(this@ActivityTambahPelanggan, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        tp_image.setOnClickListener {
            getPickPhotoFromPhone()
        }

        btn_tp_tambah.setOnClickListener {
            val namaPelanggan = et_nama_pelanggan.text.toString()
            val jamIstirahat = et_jam_istirahat.text.toString()
            val alamatPemesan = et_alamat_pemesan.text.toString()
            val alamatDikirim = et_alamat_dikirim.text.toString()
            val telepon = et_telepon.text.toString()
            val kordinat = et_kordinat.text.toString()

            when {
                namaPelanggan.isEmpty() -> et_nama_pelanggan.error = "Tidak boleh kosong"
                jamIstirahat.isEmpty() -> et_jam_istirahat.error = "Tidak boleh kosong"
                alamatPemesan.isEmpty() -> et_alamat_pemesan.error = "Tidak boleh kosong"
                alamatDikirim.isEmpty() -> et_alamat_dikirim.error = "Tidak boleh kosong"
                telepon.isEmpty() -> et_telepon.error = "Tidak boleh kosong"
                kordinat.isEmpty() -> et_kordinat.error = "Tidak boleh kosong"
                else -> {
                    addPelangganToFirebase(
                        namaPelanggan,
                        jamIstirahat,
                        alamatPemesan,
                        alamatDikirim,
                        telepon,
                        kordinat
                    )

                }
            }
        }
    }

    private fun addPelangganToFirebase(
        namaPelanggan: String,
        jamIstirahat: String,
        alamatPemesan: String,
        alamatDikirim: String,
        telepon: String,
        kordinat: String
    ) {
        // animasi loading dijalankan
        tp_loading.visibility = View.VISIBLE

        // upload photo to database firestore
        val photoName = UUID.randomUUID().toString()
        val uploadFirebase = FirebaseStorage.getInstance().getReference("rds/toko/$photoName")

        uploadFirebase.putFile(PHOTO_URI!!)
            .addOnSuccessListener {
                uploadFirebase.downloadUrl.addOnSuccessListener {
                    // ambil uri photo Firestore

                    addToDatabaseFirestore(
                        it.toString(),
                        namaPelanggan,
                        jamIstirahat,
                        alamatPemesan,
                        alamatDikirim,
                        telepon,
                        kordinat)
                }

            }

    }

    private fun addToDatabaseFirestore(
        photoUrl: String,
        namaPelanggan: String,
        jamIstirahat: String,
        alamatPemesan: String,
        alamatDikirim: String,
        telepon: String,
        kordinat: String
    ) {
        // menambah ke database realtime
        val uidPelanggan = UUID.randomUUID().toString()
        val db = FirebaseDatabase.getInstance().getReference("pelanggan/$uidPelanggan")

        db.setValue(
            PelangganNew(
                photo = photoUrl,
                namaPelanggan = namaPelanggan,
                jamIstirahat = jamIstirahat,
                alamatPemesan = alamatPemesan,
                alamatDikirim = alamatDikirim,
                telepon = telepon,
                kordinat = kordinat)
        )
            .addOnSuccessListener {
                tp_loading.visibility = View.GONE
                Toast.makeText(this, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show()

                et_nama_pelanggan.text = null
                et_jam_istirahat.text = null
                et_alamat_pemesan.text = null
                et_alamat_dikirim.text = null
                et_telepon.text = null
                et_kordinat.text = null
            }
            .addOnFailureListener{
                tp_loading.visibility = View.GONE
                Toast.makeText(this, "Gagal Menambah Data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getPickPhotoFromPhone() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO){
            if (resultCode == Activity.RESULT_OK && data?.data != null) {
                PHOTO_URI = data.data
                try {
                    PHOTO_URI?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            @Suppress("DEPRECATION")
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                PHOTO_URI
                            )
                            tp_image.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, PHOTO_URI!!)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            tp_image.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}