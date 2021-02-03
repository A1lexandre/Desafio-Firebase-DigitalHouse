package com.android.desafiojogos4.model.game

data class Game(
    val name: String,
    val description: String,
    val launchYear: Int,
    val userId: String = "",
    val ownerName: String = ""
) {
    constructor(): this(
        "",
        "",
        0,
        "",
        ""
    )
}
