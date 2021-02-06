package com.android.desafiojogos4.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.desafiojogos4.R
import com.android.desafiojogos4.adapter.GameAdapter
import com.android.desafiojogos4.databinding.ActivityHomeBinding
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.utils.Constants.firebase.GAME
import com.android.desafiojogos4.view.viewmodel.GameViewModel
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var gameViewModel: GameViewModel
    private var filterList: List<Game>? = null
    private val gameAdapter by lazy {
        GameAdapter(mutableListOf()) {
            showGameDetail(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        setupRecyclerView()
        setupButtonListeners()
        setupsearchFieldListener()
    }

    private fun setupRecyclerView() {
        binding.rvGameList.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = gameAdapter
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

    private fun showGameDetail(game: Game) {
        val gameDetail = Intent(this@HomeActivity, GameDetail::class.java)
        gameDetail.putExtra(GAME, game)
        startActivity(gameDetail)
    }

    private fun setupsearchFieldListener() = with(binding) {
        tietSearch.doOnTextChanged { text, start, before, count ->
            filterList?.let {
                val list = mutableListOf<Game>()
                for(game in it) {
                    if (game.name.toLowerCase(Locale.ROOT).contains(text.toString().toLowerCase(Locale.ROOT)))
                        list.add(game)
                }

                gameAdapter.updateList(list)
            }
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
                    filterList = list
                    gameAdapter.updateList(list as MutableList<Game>)
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