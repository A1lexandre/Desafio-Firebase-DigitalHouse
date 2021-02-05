package com.android.desafiojogos4.repository

import android.net.Uri
import android.util.Log
import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.model.game.Game
import com.android.desafiojogos4.model.user.User
import com.android.desafiojogos4.utils.Constants.firebase.ERRO_GET_GAMES
import com.android.desafiojogos4.utils.Constants.firebase.ERRO_SAVE_GAME
import com.android.desafiojogos4.utils.Constants.firebase.GAMES
import com.android.desafiojogos4.utils.Constants.firebase.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.lang.String.format
import java.time.Instant
import java.time.format.DateTimeFormatter

class GameRepository {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db by lazy {
        Firebase.firestore
    }

    private val storage by lazy {
        Firebase.storage
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

    suspend fun saveGame(game: Game, image: Uri?): FirebaseResponse {
        lateinit var resp: FirebaseResponse
        lateinit var userRef: DocumentSnapshot
        var imageUrl = ""
        try {
            auth.currentUser?.let { firebaseUser ->
                val collectionSize = getGameCollectionSize(firebaseUser.uid)
                image?.let { imageUri ->
                    val imageRef = storage.reference.child("games/${firebaseUser.uid}${collectionSize + 1}.jpg")
                    var ok = false
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener {
                                ok = true
                            }.await()
                    if(ok)
                        imageRef.downloadUrl
                                .addOnSuccessListener {
                                    imageUrl = it.toString()
                                }.await()
                }
                userRef = db.collection(USERS).document(firebaseUser.uid).get().await()
                val user = userRef.toObject(User::class.java)
                db.collection(GAMES).document("${firebaseUser.uid}${collectionSize + 1}").set(Game(game.name, game.description, game.launchYear, firebaseUser.uid, user?.name ?: "", imageUrl, "${firebaseUser.uid}${collectionSize + 1}"))
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful)
                            resp = FirebaseResponse.OnSuccess(game.name)
                        else
                            resp = FirebaseResponse.OnFailure(task.exception?.localizedMessage ?: ERRO_SAVE_GAME)
                    }
                    .await()
            }
        } catch (e: Exception) {
            resp = FirebaseResponse.OnFailure(e.localizedMessage ?: ERRO_SAVE_GAME)
        }
        return resp
    }

    suspend fun updateGame(game: Game, image: Uri?): FirebaseResponse {
        lateinit var resp: FirebaseResponse
        lateinit var userRef: DocumentSnapshot
        var imageUrl: String? = null
        try {
            auth.currentUser?.let { firebaseUser ->
                image?.let { imageUri ->
                    val imageRef = storage.reference.child("games/${game.id}.jpg")
                    var ok = false
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener {
                                ok = true
                            }.await()
                    if(ok)
                        imageRef.downloadUrl
                                .addOnSuccessListener {
                                    imageUrl = it.toString()
                                }.await()
                }
                userRef = db.collection(USERS).document(firebaseUser.uid).get().await()
                val user = userRef.toObject(User::class.java)
//                db.collection(GAMES).document(game.id).set(Game(game.name, game.description, game.launchYear, firebaseUser.uid, user?.name ?: "", imageUrl ?: game.imageUrl , game.id))
                  db.collection(GAMES).document(game.id).update(
                          hashMapOf<String, Any>(
                                  "name" to game.name,
                                  "description" to game.description,
                                  "launchYear" to game.launchYear,
                                  "imageUrl" to (imageUrl ?: game.imageUrl)
                          )
                  )
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                                resp = FirebaseResponse.OnSuccess(game.name)
                            else
                                resp = FirebaseResponse.OnFailure(task.exception?.localizedMessage ?: ERRO_SAVE_GAME)
                        }
                        .await()
            }
        } catch (e: Exception) {
            resp = FirebaseResponse.OnFailure(e.localizedMessage ?: ERRO_SAVE_GAME)
        }
        return resp
    }

    suspend fun getGameCollectionSize(id: String) : Int {
        var size = 0
        db.collection(GAMES).whereEqualTo("userId", id).get()
                .addOnSuccessListener {
                    size = it.size()
                }
                .await()

        return size
    }

}