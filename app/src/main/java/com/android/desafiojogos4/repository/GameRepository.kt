package com.android.desafiojogos4.repository

import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.utils.Constants.firebase.ERRO_GET_GAMES
import com.android.desafiojogos4.utils.Constants.firebase.GAMES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class GameRepository {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db by lazy {
        Firebase.firestore
    }

    suspend fun getGames(): FirebaseResponse {
        lateinit var resp: FirebaseResponse
        try {
            val gamesRef = db.collection(GAMES).get().await()
            if(!gamesRef.isEmpty) {
                val customGames = gamesRef.toObjects<Game>()
                resp = FirebaseResponse.OnSuccess(customGames)
            } else
                resp = FirebaseResponse.OnSuccess()
        } catch (e: Exception) {
            resp = FirebaseResponse.OnFailure(e.localizedMessage ?: ERRO_GET_GAMES)
        }

        return resp
    }
}