package com.example.appescaladegame.modele

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Mur(
    var difficulte : Difficulte,
    var nbEssais: Int,
    @Contextual
    var dateComplete : LocalDateTime
)

enum class Difficulte {
    V0,
    V1,
    V2,
    V3,
    V4,
    V5,
    V6,
    V7,
    V8,
    V9,
    V10,
    V11,
    V12,
    V13,
    V14,
    V15,
    V16,
    V17
}