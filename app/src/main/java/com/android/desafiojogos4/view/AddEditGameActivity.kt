package com.android.desafiojogos4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityAddEditGameBinding

class AddEditGameActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddEditGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}