package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity: AppCompatActivity() {

    //lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splash_screen.alpha = 0f

        splash_screen.animate().setDuration(1700).alpha(1f).withEndAction{
            val intent = Intent(this@SplashScreenActivity, LoginActivity:: class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }


        /*
        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, LoginActivity:: class.java)
            startActivity(intent)
            finish()
        }, 3000)  //delay 3 detik

         */

    }
}