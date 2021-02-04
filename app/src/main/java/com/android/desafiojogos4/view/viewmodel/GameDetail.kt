package com.android.desafiojogos4.view.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.desafiojogos4.databinding.ActivityGameDetailBinding

class GameDetail : AppCompatActivity() {

    private lateinit var binding: ActivityGameDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}