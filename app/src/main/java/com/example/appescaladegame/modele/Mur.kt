package com.example.appescaladegame.modele

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class Mur(
    var difficulte : String,
    var nbEssais: Int,
    var dateComplete : LocalDateTime,
    var score : Double
)
