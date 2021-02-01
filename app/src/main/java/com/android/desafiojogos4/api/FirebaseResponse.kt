package com.android.desafiojogos4.api

sealed class FirebaseResponse {
    class OnSuccess(val data: Any = ""): FirebaseResponse()
    class OnFailure(val message: String): FirebaseResponse()
}
