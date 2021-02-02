package com.android.desafiojogos4.model.user

data class User(
    val name: String,
    val email: String
) {
    constructor(): this("", "")
}
