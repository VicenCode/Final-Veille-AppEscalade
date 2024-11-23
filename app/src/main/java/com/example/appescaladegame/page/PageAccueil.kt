package com.example.appescaladegame.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.appescaladegame.fileService

@Composable
fun PageAccueil(navController: NavController) {
    Column {
        Text(text = "Bienvenu sur la page d'Acceuil!, ${fileService.utilisateurInBD.userName}")
    }
}