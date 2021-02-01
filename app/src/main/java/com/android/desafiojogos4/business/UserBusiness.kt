package com.android.desafiojogos4.business

import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.model.UserLogin
import com.android.desafiojogos4.model.UserRegistration
import com.android.desafiojogos4.repository.UserRepository

class UserBusiness {

    private val userRepository by lazy {
        UserRepository()
    }

    suspend fun createUser(user: UserRegistration): FirebaseResponse {
        val response =  userRepository.createUser(user)
        return when(response) {
            is FirebaseResponse.OnSuccess -> {
                FirebaseResponse.OnSuccess((response.data as String).split(" ")[0])
            }
            is FirebaseResponse.OnFailure -> {
                response
            }
        }
    }

    suspend fun loginUser(user: UserLogin): FirebaseResponse {
        val response = userRepository.loginUser(user)
        return when(response) {
            is FirebaseResponse.OnSuccess -> {
                FirebaseResponse.OnSuccess((response.data as String).split(" ")[0])
            }
            is FirebaseResponse.OnFailure -> {
                response
            }
        }
    }
}