package com.android.desafiojogos4.model.game

data class Game(
    val id: String,
    val name: String,
    val description: String,
    val launchYear: Int,
    val userId: String
) {
    constructor(): this(
        "",
        "",
        "",
        0,
        ""
    )
}
