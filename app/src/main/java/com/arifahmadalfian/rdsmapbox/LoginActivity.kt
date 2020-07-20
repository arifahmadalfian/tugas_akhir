package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arifahmadalfian.rdsmapbox.database.Database
import kotlinx.android.synthetic.main.activity_about.view.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    var database: Database? = null
    var username: EditText? = null
    var password: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        database = Database(this)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        login.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        val usr = username.toString().trim().toLowerCase(Locale.ROOT)
        val pwd = password.toString().trim().toLowerCase(Locale.ROOT)
        val loginUser = database?.checkLogin(usr,pwd )
        if (loginUser != true){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra(HomeActivity.H_CHECK_USER_LOGIN, "true")
            startActivity(intent)
        } else {
            Toast.makeText(this@LoginActivity,"Username dan Password Salah", Toast.LENGTH_SHORT).show()
        }
    }

}
