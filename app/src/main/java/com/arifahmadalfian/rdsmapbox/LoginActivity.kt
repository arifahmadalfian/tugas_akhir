package com.arifahmadalfian.rdsmapbox

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arifahmadalfian.rdsmapbox.database.Database
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_about.view.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity(){

    private val firebase = FirebaseAuth.getInstance()
    @Suppress("DEPRECATION")
    var progresDialog: ProgressDialog? = null

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
            //loading di jalankan
            //toProgresDialog()

            //loading di hentikan
           // progresDialog?.dismiss()
        }
    }
/*
    private fun toProgresDialog() {
        @Suppress("DEPRECATION")
        progresDialog = ProgressDialog(this)
        progresDialog?.show()
        progresDialog?.setContentView(R.layout.progres_dialog)
        progresDialog?.window?.setBackgroundDrawableResource(
            android.R.color.transparent
        )
    }

 */

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
