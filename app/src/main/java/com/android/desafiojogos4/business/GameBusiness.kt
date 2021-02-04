package com.android.desafiojogos4.business

import android.net.Uri
import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.repository.GameRepository

class GameBusiness {

    private val gameRepository by lazy {
        GameRepository()
    }

    suspend fun getGames(): FirebaseResponse {
        val response = gameRepository.getGames()
        return when(response) {
            is FirebaseResponse.OnSuccess -> {
                if (response.data == "")
                    FirebaseResponse.OnSuccess(listOf<Game>())
                else
                    FirebaseResponse.OnSuccess(response.data)
            }
            is FirebaseResponse.OnFailure -> {
                response
            }
        }
    }

    suspend fun saveGame(game: Game, image: Uri?): FirebaseResponse {
        return when (val response = gameRepository.saveGame(game, image)) {
            is FirebaseResponse.OnSuccess -> {
                FirebaseResponse.OnSuccess("${response.data} foi adicionado!")
            }
            is FirebaseResponse.OnFailure -> {
                response
            }
        }
    }
}