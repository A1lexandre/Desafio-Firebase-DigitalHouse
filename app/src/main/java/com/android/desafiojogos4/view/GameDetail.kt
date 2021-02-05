package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityGameDetailBinding
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.utils.Constants.firebase.GAME
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class GameDetail : AppCompatActivity() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var binding: ActivityGameDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<Game>(GAME)?.let {
            setGameInfo(it)
        }

        setupButtonListeners()
    }

    private fun setupButtonListeners() = with(binding) {

        btnBack.setOnClickListener {
            finish()
        }

        btnEdit.setOnClickListener {
            val editGame = Intent(this@GameDetail, AddEditGameActivity::class.java)
            editGame.putExtra(GAME, intent.getParcelableExtra<Game>(GAME))
            startActivity(editGame)
            finish()
        }
    }

    private fun setGameInfo(game: Game) = with(binding) {
        Glide.with(root.context).load(game.imageUrl).placeholder(R.drawable.game_icon).into(imGamePicture)
        tvGameNamePic.text = game.name
        tvGameNameDesc.text = game.name
        val launch = "Lançamento: ${game.launchYear}"
        tvLaunchYear.text = launch
        tvGameDescription.text = game.description

        var ownerName = ""

        auth.currentUser?.let {
            if(!game.userId.equals(it.uid)) {
                binding.btnEdit.visibility = View.GONE
                ownerName = "Adicionado por ${game.ownerName}"
            } else
                ownerName = "Adicionado por você"

        }

        tvOwnerName.text = ownerName

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}