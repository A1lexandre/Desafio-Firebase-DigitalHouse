package com.android.desafiojogos4.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding.btnAddUpdateGame.setOnClickListener {
            with(binding) {
                val isFormValid = listOf<Boolean>(Validation.checkField(tilName, tietName, listOf(REQUIRED)))

                if (!isFormValid.contains(false))
                    saveGame(Game(tietName.text.toString(), tietDescription.text.toString(), tietYear.text.toString().toInt()))
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
}