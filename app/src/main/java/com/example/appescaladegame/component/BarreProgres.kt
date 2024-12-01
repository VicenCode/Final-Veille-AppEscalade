package com.example.appescaladegame.component

import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.appescaladegame.fileService
import com.example.appescaladegame.modele.Utilisateur
import com.example.appescaladegame.ui.theme.AccentGreen01
import com.example.appescaladegame.ui.theme.AccentGreen02
import kotlinx.coroutines.delay
import kotlin.math.pow

@Composable
fun BarreProgres() {
    var currentProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        loadProgress { progress ->
            currentProgress = progress
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(1500, easing = EaseOutElastic)
    )

    LinearProgressIndicator(
        progress = animatedProgress,
        Modifier.fillMaxWidth().height(10.dp),
        color = AccentGreen02,
        trackColor = AccentGreen01,
        strokeCap = StrokeCap.Square
    )

}

suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    val utilisateur = fileService.utilisateurInBD;
    val expNextLevel = (utilisateur.progression.niveauActuel + 1 / 0.05).pow(2.0)

    val difference = (100 * utilisateur.progression.expActuelle) / expNextLevel;

    for (i in 0..difference.toInt()) {
        updateProgress(i.toFloat() / 100)
        delay(10)
    }
}