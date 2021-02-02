package com.android.desafiojogos4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.desafiojogos4.databinding.GameCardBinding
import com.android.desafiojogos4.model.game.Game
import com.bumptech.glide.Glide

class GameAdapter(val list: MutableList<Game>): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(GameCardBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: GameCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun binding(game: Game) = with(binding) {
            tvGameName.text = game.name
            tvGameLaunchYear.text = game.launchYear.toString()
        }
    }
}