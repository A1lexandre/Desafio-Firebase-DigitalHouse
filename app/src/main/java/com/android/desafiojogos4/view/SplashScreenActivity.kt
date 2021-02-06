package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val splashtime = 3000L
    private lateinit var handler: Handler

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            auth.currentUser?.let {
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
            } ?: run {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, splashtime)
    }
}