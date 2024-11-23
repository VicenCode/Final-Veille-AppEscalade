package com.example.appescaladegame.modele

import kotlinx.serialization.Serializable

@Serializable
data class Progression(
    var expActuelle : Double,
    var niveauActuel : Int
)