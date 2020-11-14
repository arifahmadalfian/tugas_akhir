package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.agrawalsuneet.dotsloader.loaders.TashieLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    private val firebase = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        loading.addView(tashie)
        loading.visibility = View.GONE

        btn_login_login.setOnClickListener {
            val email = et_login_email.text.toString().trim()
            val password = et_login_password.text.toString().trim()

            when {
                email.isEmpty() -> et_login_email.error = "Email tidak boleh kosong"
                password.isEmpty() -> et_login_password.error = "Password tidak boleh kosong"
                else -> loginToFirebase()
            }

        }

        btn_login_to_register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginToFirebase() {
        loading.visibility = View.VISIBLE
        val email = et_login_email.text.toString().trim()
        val password = et_login_password.text.toString().trim()
        firebase.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity,"Username dan Password Salah",Toast.LENGTH_SHORT).show()
                }
                loading.visibility = View.GONE
            }
            .addOnFailureListener{
                Toast.makeText(this@LoginActivity,"Tidak terhubung internet",Toast.LENGTH_SHORT).show()
                loading.visibility = View.GONE
            }
    }

}
