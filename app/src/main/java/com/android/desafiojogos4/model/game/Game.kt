package com.android.desafiojogos4.model.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val name: String,
    val description: String,
    val launchYear: Int,
    val userId: String = "",
    val ownerName: String = "",
    val imageUrl: String = "",
    val id: String = ""
): Parcelable {
    constructor(): this(
        "",
        "",
        0,
        "",
        "",
        "",
        ""
    )
}
