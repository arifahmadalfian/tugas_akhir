package com.arifahmadalfian.rdsmapbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arifahmadalfian.rdsmapbox.activity.Home
import com.arifahmadalfian.rdsmapbox.activity.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splash_screen.alpha = 0f

        splash_screen.animate().setDuration(1700).alpha(1f).withEndAction{
            val intent = Intent(this@SplashScreenActivity, Home:: class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }
}