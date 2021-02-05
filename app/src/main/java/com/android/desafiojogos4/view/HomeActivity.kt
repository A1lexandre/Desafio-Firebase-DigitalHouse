package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.desafiojogos4.R
import com.android.desafiojogos4.adapter.GameAdapter
import com.android.desafiojogos4.databinding.ActivityHomeBinding
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.utils.Constants.firebase.GAME
import com.android.desafiojogos4.view.viewmodel.GameViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        setupButtonListeners()
    }

    private fun setupRecyclerView(list: MutableList<Game>) {
        binding.rvGameList.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = GameAdapter(list) {
                val gameDetail = Intent(this@HomeActivity, GameDetail::class.java)
                gameDetail.putExtra(GAME, it)
                startActivity(gameDetail)
            }
        }
    }

    private fun setupButtonListeners() = with(binding) {

        btnAddGame.setOnClickListener{
            startActivity(Intent(this@HomeActivity, AddEditGameActivity::class.java))
            overridePendingTransition(R.anim.slide_in_down, R.anim.stay)
        }

        btnSignOut.setOnClickListener {
            gameViewModel.signOut()

            gameViewModel.gameSucess.observe(this@HomeActivity, {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            })

            gameViewModel.gameSucess.observe(this@HomeActivity, {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            })
        }

    }

    private fun getGames() {
        gameViewModel.getGames()

        gameViewModel.getGameSucess.observe(this, { list ->
            binding.loadingView.visibility = View.GONE
                if(list.isEmpty()) {
                    binding.emptySaying.visibility = View.VISIBLE
                } else {
                    binding.emptySaying.visibility = View.GONE
                    setupRecyclerView(list as MutableList<Game>)
                }
        })

        gameViewModel.gameFailure.observe(this, {
            binding.loadingView.visibility = View.GONE
            Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
        getGames()
    }
}