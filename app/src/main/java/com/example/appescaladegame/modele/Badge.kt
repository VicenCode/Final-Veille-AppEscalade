package com.example.appescaladegame.modele

import kotlinx.serialization.Serializable

@Serializable
data class Badge(
    var titre : String,
    var nomImage : String,
)