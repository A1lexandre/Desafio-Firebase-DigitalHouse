package com.android.desafiojogos4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.desafiojogos4.R

class UserRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}