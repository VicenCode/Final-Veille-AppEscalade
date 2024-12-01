package com.example.appescaladegame.modele

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import com.example.appescaladegame.ui.theme.Dark
import kotlinx.serialization.Serializable


@Serializable
data class Titre(var titre : String, var couleur : List<Long>)