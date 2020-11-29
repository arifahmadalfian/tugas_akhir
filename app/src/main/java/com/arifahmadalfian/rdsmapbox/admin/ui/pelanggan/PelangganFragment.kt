package com.arifahmadalfian.rdsmapbox.admin.ui.pelanggan

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.arifahmadalfian.rdsmapbox.R
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_pelanggan.*
import java.util.*

class PelangganFragment : Fragment() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var PICK_PHOTO = 100
    private var PICK_CAMERA = 101
    private var PHOTO_URI: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pelanggan, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // menambahkan circle loader/loading
        val tashie = context?.let { ContextCompat.getColor(it, R.color.colorLoginPrimaryDark) }?.let {
            TashieLoader(
                context, 5,
                30, 10,
                it
            )
                .apply {
                    animDuration = 500
                    animDelay = 50
                    interpolator = LinearInterpolator()
                }
        }
        tp_loading.addView(tashie)
        tp_loading.visibility = View.GONE

        initView()
    }

    private fun initView() {

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
            Pelanggan(
                photo = photoUrl,
                nama = namaPelanggan,
                keterangan = jamIstirahat,
                alamat_pemesan = alamatPemesan,
                alamat_dikirim = alamatDikirim,
                telepon = telepon,
                koordinat = kordinat)
        )
            .addOnSuccessListener {
                tp_loading.visibility = View.GONE
                Toast.makeText(context, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show()

                et_nama_pelanggan.text = null
                et_jam_istirahat.text = null
                et_alamat_pemesan.text = null
                et_alamat_dikirim.text = null
                et_telepon.text = null
                et_kordinat.text = null
            }
            .addOnFailureListener{
                tp_loading.visibility = View.GONE
                Toast.makeText(context, "Gagal Menambah Data", Toast.LENGTH_SHORT).show()
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
                                activity?.contentResolver,
                                PHOTO_URI
                            )
                            tp_image.setImageBitmap(bitmap)
                        } else {
                            val source = activity?.contentResolver?.let { it1 ->
                                ImageDecoder.createSource(
                                    it1, PHOTO_URI!!)
                            }
                            val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
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