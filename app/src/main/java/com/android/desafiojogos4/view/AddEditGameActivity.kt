package com.android.desafiojogos4.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityAddEditGameBinding
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.utils.Constants.firebase.GAME
import com.android.desafiojogos4.validation.Validation
import com.android.desafiojogos4.validation.Validation.Companion.REQUIRED
import com.android.desafiojogos4.view.viewmodel.GameViewModel
import com.bumptech.glide.Glide

class AddEditGameActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddEditGameBinding
    lateinit var gameViewModel: GameViewModel
    var imageUri: Uri? = null
    var edit = false
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        intent.getParcelableExtra<Game>(GAME)?.let {
            setGameInfo(it)
            edit = true
            game = it
        }

        setupButtonClickListeners()
    }

    private fun setGameInfo(game: Game) = with(binding) {
        Glide.with(root.context).load(game.imageUrl).placeholder(R.drawable.game_icon).into(profileImage)
        tietName.text = Editable.Factory.getInstance().newEditable(game.name)
        tietYear.text = Editable.Factory.getInstance().newEditable(game.launchYear.toString())
        tietDescription.text = Editable.Factory.getInstance().newEditable(game.description)
    }

    private fun setupButtonClickListeners() {

        with(binding) {
            btnAddUpdateGame.setOnClickListener {
                val isFormValid = listOf<Boolean>(Validation.checkField(tilName, tietName, listOf(REQUIRED)), Validation.checkField(tilYear, tietYear, listOf(REQUIRED)))

                if (!isFormValid.contains(false))
                    if (!edit)
                    saveGame(Game(tietName.text.toString().trim(), tietDescription.text.toString().trim(), tietYear.text.toString().trim().toInt()))
                else
                    updateGame(Game(tietName.text.toString().trim(), tietDescription.text.toString().trim(), tietYear.text.toString().trim().toInt(), game.userId, game.ownerName, game.imageUrl, game.id))
            }

            profileImage.setOnClickListener {
                openGallery()
            }

        }

    }

    private fun saveGame(game: Game) = with(gameViewModel) {
        saveGame(game, imageUri)

        gameSucess.observe(this@AddEditGameActivity, {
            Toast.makeText(this@AddEditGameActivity, it, Toast.LENGTH_SHORT).show()
            finish()
        })

        gameFailure.observe(this@AddEditGameActivity, {
            Toast.makeText(this@AddEditGameActivity, it, Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun updateGame(game: Game) = with(gameViewModel) {
        updateGame(game, imageUri)

        gameSucess.observe(this@AddEditGameActivity, {
            Toast.makeText(this@AddEditGameActivity, it, Toast.LENGTH_SHORT).show()
            finish()
        })

        gameFailure.observe(this@AddEditGameActivity, {
            Toast.makeText(this@AddEditGameActivity, it, Toast.LENGTH_SHORT).show()
            finish()
        })

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            binding.profileImage.setImageURI(imageUri)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
    }

    companion object {
        const val PICK_IMAGE = 23
    }
}