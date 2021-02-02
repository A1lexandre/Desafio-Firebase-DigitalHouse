package com.android.desafiojogos4.repository

import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.model.user.User
import com.android.desafiojogos4.model.user.UserLogin
import com.android.desafiojogos4.model.user.UserRegistration
import com.android.desafiojogos4.utils.Constants.firebase.LOGIN_SEM_SUCESSO
import com.android.desafiojogos4.utils.Constants.firebase.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db by lazy {
        Firebase.firestore
    }

    suspend fun createUser(user: UserRegistration): FirebaseResponse {
        lateinit var resp: FirebaseResponse
        try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            if (auth.currentUser != null)
                auth.currentUser?.let {
                    db.collection(USERS).document(it.uid).set(User(user.name, user.email)).await()
                    resp = FirebaseResponse.OnSuccess(user.name)
                }
            else
                resp = FirebaseResponse.OnFailure(LOGIN_SEM_SUCESSO)
        } catch(e: Exception) {
            resp = FirebaseResponse.OnFailure(e.localizedMessage ?: "")
        }

        return resp
    }

    suspend fun loginUser(user: UserLogin): FirebaseResponse {
        lateinit var resp: FirebaseResponse
        try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            if (auth.currentUser != null)
                auth.uid?.let {
                    val docRef = db.collection("users").document(it).get().await()
                    val user = docRef.toObject<User>()
                    resp = FirebaseResponse.OnSuccess(user?.name ?: "")
                }
            else
                resp = FirebaseResponse.OnFailure(LOGIN_SEM_SUCESSO)
        } catch(e: Exception) {
            resp = FirebaseResponse.OnFailure(e.localizedMessage ?: "")
        }

    return resp
    }
}


