package com.android.desafiojogos4.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.desafiojogos4.api.FirebaseResponse
import com.android.desafiojogos4.business.GameBusiness
import com.android.desafiojogos4.model.game.Game
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

    val gameSucess = MutableLiveData<List<Game>>()
    val gameFailure = MutableLiveData<String>()

    private val gameBusiness by lazy {
        GameBusiness()
    }

    fun getGames() {
        viewModelScope.launch {
            when(val response = gameBusiness.getGames()) {
                is FirebaseResponse.OnSuccess -> {
                    gameSucess.postValue(
                        (response.data as? List<Game>) ?: mutableListOf<Game>()
                    )
                }
                is FirebaseResponse.OnFailure -> {
                    gameFailure.postValue(
                        response.message
                    )
                }
            }
        }
    }

}