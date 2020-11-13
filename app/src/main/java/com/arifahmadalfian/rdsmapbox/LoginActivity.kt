package com.arifahmadalfian.rdsmapbox

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    private val firebase = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        }
    }

    private fun loginToFirebase() {

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
            }
            .addOnFailureListener{
                Toast.makeText(this@LoginActivity,"Tidak terhubung internet",Toast.LENGTH_SHORT).show()
            }
    }

}
