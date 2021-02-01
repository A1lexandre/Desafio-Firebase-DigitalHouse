package com.android.desafiojogos4.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.business.UserBusiness
import com.android.desafiojogos4.model.UserLogin
import com.android.desafiojogos4.model.UserRegistration
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val userSucess = MutableLiveData<String>()
    val userFailure = MutableLiveData<String>()

    private val userBusiness by lazy {
        UserBusiness()
    }

    fun createUser(user: UserRegistration) {
        viewModelScope.launch {
            val response = userBusiness.createUser(user)
            when(response) {
                is FirebaseResponse.OnSuccess -> {
                    userSucess.postValue(
                        response.data as? String
                    )
                }
                is FirebaseResponse.OnFailure -> {
                    userFailure.postValue(
                        response.message
                    )
                }
            }
        }
    }

    fun loginUser(user: UserLogin) {
        viewModelScope.launch {
            val response = userBusiness.loginUser(user)
            when(response) {
                is FirebaseResponse.OnSuccess -> {
                    userSucess.postValue(
                        response.data as? String
                    )
                }
                is FirebaseResponse.OnFailure -> {
                    userFailure.postValue(
                        response.message
                    )
                }
            }
        }
    }

}