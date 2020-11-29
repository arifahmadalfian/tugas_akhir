package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arifahmadalfian.rdsmapbox.admin.AdminActivity
import kotlinx.android.synthetic.main.activity_login_admin.*

class LoginAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        btn_login_admin.setOnClickListener {
            val intent = Intent(this@LoginAdminActivity, AdminActivity::class.java)
            startActivity(intent)
        }
    }
}