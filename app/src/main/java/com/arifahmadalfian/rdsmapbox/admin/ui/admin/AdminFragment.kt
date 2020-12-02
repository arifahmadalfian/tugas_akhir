package com.arifahmadalfian.rdsmapbox.admin.ui.admin

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
import com.arifahmadalfian.rdsmapbox.model.Admin
import com.arifahmadalfian.rdsmapbox.model.Pengirim
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_admin.*
import kotlinx.android.synthetic.main.fragment_pengirim.*
import java.util.*

class AdminFragment : Fragment() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    var PICK_PHOTO = 100
    var PICK_CAMERA = 101
    var PHOTO_URI: Uri? = null

    var UIDAdmin: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin, container, false)
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
        tp_loading_admin.addView(tashie)
        tp_loading_admin.visibility = View.GONE

        initView()
    }

    private fun initView() {
        img_admin.setOnClickListener {
            getPickPhotoFromPhone()
        }

        btn_tambah_admin.setOnClickListener {
            val nama = et_nama__admin.text.toString()
            val email = et_email_admin.text.toString()
            val password = et_password_admin.text.toString()
            val telepon = et_telepon_admin.text.toString()
            val alamat = et_alamat_admin.text.toString()


            when {
                nama.isEmpty() -> et_nama__admin.error = "Tidak boleh kosong"
                email.isEmpty() -> et_email_admin.error = "Tidak boleh kosong"
                password.isEmpty() -> et_password_admin.error = "Tidak boleh kosong"
                telepon.isEmpty() -> et_telepon_admin.error = "Tidak boleh kosong"
                alamat.isEmpty() -> et_alamat_admin.error = "Tidak boleh kosong"
                else -> {
                    addAdminToFirebase(
                        nama,
                        email,
                        password,
                        telepon,
                        alamat
                    )

                }
            }
        }

    }

    private fun addAdminToFirebase(
        nama: String,
        email: String,
        password: String,
        telepon: String,
        alamat: String) {

        // animasi loading dijalankan
        tp_loading_admin.visibility = View.VISIBLE

        registerPengirimToFirebase(email, password)

        // upload photo to database firestore
        UIDAdmin = UUID.randomUUID().toString()
        val uploadFirebase = FirebaseStorage.getInstance().getReference("rds/admin/$UIDAdmin")

        uploadFirebase.putFile(PHOTO_URI!!)
            .addOnSuccessListener {
                uploadFirebase.downloadUrl.addOnSuccessListener {
                    // ambil uri photo Firestore

                    addToDatabaseFirestore(
                        it.toString(),
                        nama,
                        email,
                        password,
                        telepon,
                        alamat)
                }

            }
    }

    private fun addToDatabaseFirestore(
        photoUrl: String,
        nama: String,
        email: String,
        password: String,
        telepon: String,
        alamat: String) {

        // menambah ke database realtime
        val db = FirebaseDatabase.getInstance().getReference("admin/$UIDAdmin")

        db.setValue(
            Admin(
                photo = photoUrl,
                nama = nama,
                email= email,
                password= password,
                telepon = telepon,
                alamat = alamat)
        )
            .addOnSuccessListener {
                tp_loading_admin.visibility = View.GONE
                Toast.makeText(context, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show()

                et_nama__admin.text = null
                et_email_admin.text = null
                et_password_admin.text = null
                et_telepon_admin.text = null
                et_alamat_admin.text = null
            }
            .addOnFailureListener{
                tp_loading_admin.visibility = View.GONE
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
                            img_admin.setImageBitmap(bitmap)
                        } else {
                            val source = activity?.contentResolver?.let { it1 ->
                                ImageDecoder.createSource(
                                    it1, PHOTO_URI!!)
                            }
                            val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                            img_admin.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}