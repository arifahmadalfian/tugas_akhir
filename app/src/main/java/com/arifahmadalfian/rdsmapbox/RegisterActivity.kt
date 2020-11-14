package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arifahmadalfian.rdsmapbox.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register_to_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}