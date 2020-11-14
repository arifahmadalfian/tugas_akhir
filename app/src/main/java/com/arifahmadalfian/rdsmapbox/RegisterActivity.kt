package com.arifahmadalfian.rdsmapbox

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    var PICK_PHOTO = 100
    var PICK_CAMERA = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
        register_loading.addView(tashie)
        register_loading.visibility = View.GONE

        initView()

    }

    private fun initView() {
        btn_register_to_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        register_profil.setOnClickListener {
            getPhotoFromPhone()
        }

        btn_register.setOnClickListener {
            val nama = et_register_nama.text.toString().trim()
            val email = et_register_email.text.toString().trim()
            val password = et_register_password.text.toString().trim()

            when {
                nama.isEmpty() -> et_register_nama.error = "Nama tidak boleh kosong"
                email.isEmpty() -> et_register_email.error = "Email tidak boleh kosong"
                password.isEmpty() -> et_register_password.error = "Password tidak boleh kosong"
                else -> registerUserToFirebase(email, password)
            }
        }
    }

    private fun getPhotoFromPhone() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO){
            if (resultCode == Activity.RESULT_OK && data?.data != null) {
                val selectedPhotoUri = data.data
                try {
                    selectedPhotoUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            @Suppress("DEPRECATION")
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                selectedPhotoUri
                            )
                            register_profil.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            register_profil.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun registerUserToFirebase(email: String, password: String) {
        register_loading.visibility = View.VISIBLE

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Berhasil Menambah User", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Gagal Menambah User", Toast.LENGTH_LONG).show()
                }
                register_loading.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal Menambahkan Data", Toast.LENGTH_SHORT).show()
                register_loading.visibility = View.GONE
            }
    }
}