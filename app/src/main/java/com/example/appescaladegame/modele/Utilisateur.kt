package com.example.appescaladegame.modele

import kotlinx.serialization.Serializable

@Serializable
data class Utilisateur(
    var userName : String,
    var progression: Progression,
    var titre : Titre,
    var badges: MutableList<Badge>
)