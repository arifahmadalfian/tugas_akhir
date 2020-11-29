package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_main_pengirim.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        btn_main_admin.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginAdminActivity::class.java)
            startActivity(intent)
        }
    }
}