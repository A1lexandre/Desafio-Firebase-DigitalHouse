package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.desafiojogos4.R
import com.android.desafiojogos4.databinding.ActivityAddEditGameBinding
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.validation.Validation
import com.android.desafiojogos4.validation.Validation.Companion.REQUIRED
import com.android.desafiojogos4.view.viewmodel.GameViewModel

class AddEditGameActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddEditGameBinding
    lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {

        with(binding) {
            btnAddUpdateGame.setOnClickListener {
                val isFormValid = listOf<Boolean>(Validation.checkField(tilName, tietName, listOf(REQUIRED)))

                if (!isFormValid.contains(false))
                    saveGame(Game(tietName.text.toString().trim(), tietDescription.text.toString().trim(), tietYear.text.toString().trim().toInt()))
            }

            profileImage.setOnClickListener {
                openGallery()
            }

        }

    }

    private fun saveGame(game: Game) = with(gameViewModel) {
        saveGame(game)

        gameSucess.observe(this@AddEditGameActivity, {
            Toast.makeText(this@AddEditGameActivity, it, Toast.LENGTH_SHORT).show()
            finish()
        })

        gameFailure.observe(this@AddEditGameActivity, {
            Toast.makeText(this@AddEditGameActivity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data?.data
            binding.profileImage.setImageURI(imageUri)
        }
    }

    companion object {
        const val PICK_IMAGE = 23
    }
}