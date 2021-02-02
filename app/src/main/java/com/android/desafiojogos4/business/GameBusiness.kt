package com.android.desafiojogos4.business

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
}