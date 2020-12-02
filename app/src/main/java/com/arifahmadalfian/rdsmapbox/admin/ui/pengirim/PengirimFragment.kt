package com.arifahmadalfian.rdsmapbox.admin.ui.pengirim

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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.arifahmadalfian.rdsmapbox.R
import com.arifahmadalfian.rdsmapbox.model.Pelanggan
import com.arifahmadalfian.rdsmapbox.model.Pengirim
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_pelanggan.*
import kotlinx.android.synthetic.main.fragment_pengirim.*
import java.util.*

class PengirimFragment : Fragment() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    var PICK_PHOTO = 100
    var PICK_CAMERA = 101
    var PHOTO_URI: Uri? = null

    var UIDPengirim: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengirim, container, false)
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
        tp_loading_pengirim.addView(tashie)
        tp_loading_pengirim.visibility = View.GONE

        initView()
    }

    private fun initView() {
        img_pengirim.setOnClickListener {
            getPickPhotoFromPhone()
        }

        btn_tambah_pengirim.setOnClickListener {
            val namaPengirim = et_nama_pengirim.text.toString()
            val emailPengirim = et_email_pengirim.text.toString()
            val passwordPengirim = et_password_pengirim.text.toString()
            val teleponPengirim = et_telepon_pengirim.text.toString()
            val alamatPengirim = et_alamat_pengirim.text.toString()


            when {
                namaPengirim.isEmpty() -> et_nama_pengirim.error = "Tidak boleh kosong"
                emailPengirim.isEmpty() -> et_email_pengirim.error = "Tidak boleh kosong"
                passwordPengirim.isEmpty() -> et_password_pengirim.error = "Tidak boleh kosong"
                teleponPengirim.isEmpty() -> et_telepon_pengirim.error = "Tidak boleh kosong"
                alamatPengirim.isEmpty() -> et_alamat_pengirim.error = "Tidak boleh kosong"
                else -> {
                    addPengirimToFirebase(
                        namaPengirim,
                        emailPengirim,
                        passwordPengirim,
                        teleponPengirim,
                        alamatPengirim
                    )

                }
            }
        }

    }

    private fun addPengirimToFirebase(
        namaPengirim: String,
        emailPengirim: String,
        passwordPengirim: String,
        teleponPengirim: String,
        alamatPengirim: String) {

        // animasi loading dijalankan
        tp_loading_pengirim.visibility = View.VISIBLE

        registerPengirimToFirebase(emailPengirim, passwordPengirim)

        // upload photo to database firestore
        UIDPengirim = UUID.randomUUID().toString()
        val uploadFirebase = FirebaseStorage.getInstance().getReference("rds/pengirim/$UIDPengirim")

        uploadFirebase.putFile(PHOTO_URI!!)
            .addOnSuccessListener {
                uploadFirebase.downloadUrl.addOnSuccessListener {
                    // ambil uri photo Firestore

                    addToDatabaseFirestore(
                        it.toString(),
                        namaPengirim,
                        emailPengirim,
                        passwordPengirim,
                        teleponPengirim,
                        alamatPengirim)
                }

            }
    }

    private fun addToDatabaseFirestore(
        photoUrl: String,
        namaPengirim: String,
        emailPengirim: String,
        passwordPengirim: String,
        teleponPengirim: String,
        alamatPengirim: String) {

        // menambah ke database realtime
        val db = FirebaseDatabase.getInstance().getReference("pengirim/$UIDPengirim")

        db.setValue(
            Pengirim(
                photo = photoUrl,
                nama = namaPengirim,
                email_pengirim = emailPengirim,
                password_pengirim = passwordPengirim,
                telepon = teleponPengirim,
                alamat = alamatPengirim)
        )
            .addOnSuccessListener {
                tp_loading_pengirim.visibility = View.GONE
                Toast.makeText(context, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show()

                et_nama_pengirim.text = null
                et_email_pengirim.text = null
                et_password_pengirim.text = null
                et_telepon_pengirim.text = null
                et_alamat_pengirim.text = null
            }
            .addOnFailureListener{
                tp_loading_pengirim.visibility = View.GONE
                Toast.makeText(context, "Gagal Menambah Data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerPengirimToFirebase(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

            }
            .addOnFailureListener {
               Toast.makeText(context, "Email Sudah ada atau password kurang dari 6 digit", Toast.LENGTH_SHORT).show()
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
                            img_pengirim.setImageBitmap(bitmap)
                        } else {
                            val source = activity?.contentResolver?.let { it1 ->
                                ImageDecoder.createSource(
                                    it1, PHOTO_URI!!)
                            }
                            val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                            img_pengirim.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}