package com.android.desafiojogos4.view

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
import com.android.desafiojogos4.view.viewmodel.GameViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //getGames()
        binding.rvGameList.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = GameAdapter(getGameList())
        }
        binding.loadingView.visibility = View.GONE
    }

    private fun getGameList(): MutableList<Game> {
        val list = mutableListOf<Game>()
        for(i in 1..10) {
            list.add(Game("0", "Mortal Kombat", "SomeDescription", 2019, "0"))
        }
        return list
    }

    private fun getGames() {
        gameViewModel.getGames()

        gameViewModel.gameSucess.observe(this, {
            binding.loadingView.visibility = View.GONE
            it?.let { list ->
                if(list.isEmpty()) {
                    binding.emptySaying.visibility = View.VISIBLE
                } else {
                    binding.emptySaying.visibility = View.GONE
                }
            }
        })

        gameViewModel.gameFailure.observe(this, {
            binding.loadingView.visibility = View.GONE
            Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
        })
    }
}