package com.example.appescaladegame.page

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.appescaladegame.component.BadgeItem
import com.example.appescaladegame.component.BarreProgres
import com.example.appescaladegame.fileService
import com.example.appescaladegame.ui.theme.AccentGreen02
import com.example.appescaladegame.ui.theme.Dark
import com.example.appescaladegame.ui.theme.flash
import ir.ehsannarmani.compose_charts.extensions.format
import kotlin.math.pow

@Composable
fun PageProfil(navController: NavController) {
    val utilisateur = fileService.utilisateurInBD;
    val expNextLevel = (utilisateur.progression.niveauActuel + 1 / 0.05).pow(2.0)
    var widthNom by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current;
    var lancerAnimation by remember { mutableStateOf(false) }

    val alphaTitreAnimation by animateFloatAsState(
        targetValue = if(lancerAnimation) 1f else 0f,
        animationSpec = tween(1000, easing= FastOutSlowInEasing),
        label = "Animation titre utilsateur fade-in"
    )

    val translationTitreAnimation by animateFloatAsState(
        targetValue = if(lancerAnimation) 0f else -300f,
        animationSpec = tween(1000, easing= FastOutSlowInEasing),
        label = "Animation titre utilsateur slide-in"
    )


    LaunchedEffect(Unit) {
        lancerAnimation = true;
    }



    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 20.dp)
    ) {
        Row {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Go back to Accueil")
            }

        }
        Column {
            Row(
                Modifier.onGloballyPositioned { coordinates ->
                    val widthEnPx = coordinates.size.width
                widthNom = with(density) { widthEnPx.toDp() }
            }
            ) {
                Text(
                    text = utilisateur.userName,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Dark
                )
            }

            Row (
                modifier = Modifier.width(widthNom.coerceIn(100.dp, 150.dp)),
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = utilisateur.titre.titre,
                    maxLines = 1,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        brush = Brush.verticalGradient(colors = utilisateur.titre.couleur.map { Color(it) })
                    ),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.graphicsLayer {
                        this.translationX = translationTitreAnimation
                        this.alpha = alphaTitreAnimation
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.size(30.dp))

        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Level ${utilisateur.progression.niveauActuel}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            
            Row {
                BarreProgres()
            }
            
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = utilisateur.progression.expActuelle.format(2))
                Text(text = expNextLevel.format(2))
            }

        }
        
        Spacer(modifier = Modifier.size(60.dp))
        
        Column (
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Text(
                text = "Badges",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGreen02
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 30.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(count = utilisateur.badges.size) { index ->
                    val badge = utilisateur.badges[index];
                    BadgeItem(titre = badge.titre, imageName = badge.nomImage)
                }
            }
        }


    }
}