package com.example.appescaladegame.page

import androidx.annotation.Px
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.appescaladegame.fileService
import com.example.appescaladegame.modele.Mur
import com.example.appescaladegame.ui.theme.AccentGreen01
import com.example.appescaladegame.ui.theme.AccentGreen02
import com.example.appescaladegame.ui.theme.Dark
import com.example.appescaladegame.ui.theme.flash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.times

val difficulteSet = mapOf (
    "V0" to 100.0,
    "V1" to 200.0,
    "V2" to 300.0,
    "V3" to 500.0,
    "V4" to 700.0,
    "V5" to 900.0,
    "V6" to 1200.0,
    "V7" to 1500.0,
    "V8" to 1900.0,
    "V9" to 2400.0,
    "V10" to 3000.0,
    "V11" to 3800.0,
    "V12" to 5000.0,
    "V13" to 6500.0,
);

fun calculerScore(scoreDifficulte : Double, nbEssais : Int) : Double {
    return scoreDifficulte / (1 + 0.1 * (nbEssais - 1 ));
}

fun ajouterMur(difficulte : String, nbEssais : Int){
    val now : Instant = Clock.System.now();
    val score = calculerScore(difficulteSet[difficulte] as Double, nbEssais)

    fileService.saveMur(
        Mur(
            dateComplete = now.toLocalDateTime(TimeZone.currentSystemDefault()),
            difficulte = difficulte,
            nbEssais = nbEssais,
            score = score
        )
    )
}


@Composable
fun PageAjoutMur(navController: NavController) {

    var selectedOption by remember { mutableStateOf("V0") }
    var isExpanded by remember { mutableStateOf(false) }
    var nbEssais by remember { mutableStateOf(1) }
    val openAlertDialog = remember { mutableStateOf(false) }
    val dragCompleted = remember { mutableStateOf(false) }

    var largeurBackgroundSliderParent by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val boutonWidthPx = with(density) { 60.dp.toPx() }
    val (minPos, maxPos) = 0f to remember { mutableStateOf(0f) }


    val animatedOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    when {
        openAlertDialog.value -> {
            Dialog(
                onDismissRequest = { openAlertDialog.value = false },
                //properties = DialogProperties()
            ) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ){
                    Row(
                        Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Si vous quittez, le mur ne sera pas sauvegardé",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { openAlertDialog.value = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentGreen02,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(text = "Annuler")
                        }
                        
                        Spacer(modifier = Modifier.size(10.dp))
                        
                        TextButton(
                            onClick = { navController.popBackStack(); openAlertDialog.value = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Dark
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, AccentGreen01)
                        ) {
                            Text(text = "Confirmer")
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        IconButton(onClick = {
            openAlertDialog.value = true;
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Return to Accueil")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Ajouter un Mur",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = AccentGreen02
        )

        Spacer(modifier = Modifier
            .size(50.dp)
            .fillMaxWidth()
        )

        var flashState by remember { mutableStateOf(false) }

        val flashAnimation = animateFloatAsState(
            targetValue = if(flashState) 2f else 0f,
            animationSpec = tween(1000, easing = EaseOutElastic),
            label = "Animation Mur Premier essai"
        )

        Text(
            text = "FLASH!",
            Modifier.graphicsLayer {
                this.scaleY = flashAnimation.value
                this.scaleX = flashAnimation.value
            },
            fontStyle = FontStyle.Italic,
            style = TextStyle(shadow = Shadow(Dark, offset = Offset(2f, 2f), blurRadius = 2f)),
            color = flash,
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier
            .size(50.dp)
            .fillMaxWidth()
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopCenter)
        ) {

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Difficulté",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
                
                Row (
                    Modifier
                        .clickable(onClick = { isExpanded = !isExpanded })
                        .height(40.dp)
                        .width(200.dp)
                        .border(1.dp, Dark, RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = selectedOption, style = MaterialTheme.typography.bodyLarge)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Show Difficulty Options")
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = !isExpanded },
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp),
                offset = DpOffset(50.dp, 0.dp)
            ) {
                for (difficulte in difficulteSet.keys) {
                    DropdownMenuItem(
                        text = { Text(difficulte) },
                        onClick = ({
                            selectedOption = difficulte;
                            isExpanded = !isExpanded
                        })
                    )
                }
            }
        }

        Spacer(modifier = Modifier
            .size(50.dp)
            .fillMaxWidth()
        )

        Row {
            Text(
                text = "Nombre d'essais",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier
            .size(20.dp)
            .fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = { nbEssais-- },
                enabled = nbEssais > 1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen02,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = "-", fontSize = 20.sp)
            }

            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .border(1.dp, Dark, shape = RoundedCornerShape(12.dp))
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$nbEssais",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { nbEssais++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen02,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = "+", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier
            .size(80.dp)
            .fillMaxWidth())

        Row(
            Modifier.height(60.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(AccentGreen02, RoundedCornerShape(40.dp))
                    .fillMaxSize()
                    .onGloballyPositioned { layoutCoordinates ->
                        largeurBackgroundSliderParent = layoutCoordinates.size.width
                        maxPos.value = (largeurBackgroundSliderParent - boutonWidthPx)
                    }
                    .draggable(
                        enabled = !dragCompleted.value,
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            val newValue =
                                (animatedOffset.value + delta).coerceIn(minPos, maxPos.value)
                            coroutineScope.launch {
                                animatedOffset.snapTo(newValue)
                            }
                        },
                        onDragStopped = {
                            coroutineScope.launch {
                                if (animatedOffset.value < maxPos.value * 0.95f) {
                                    animatedOffset.animateTo(
                                        minPos,
                                        animationSpec = tween(600, easing = EaseOutBounce)
                                    )
                                } else {
                                    dragCompleted.value = true;
                                    if (nbEssais == 1) {
                                        flashState = true
                                        delay(1000)
                                    }

                                    withContext(Dispatchers.Main) {
                                    ajouterMur(difficulte = selectedOption, nbEssais = nbEssais)
                                        navController.popBackStack()
                                    }

                                }
                            }
                        }
                    )
            ) {
                Box(
                    Modifier
                        .offset { IntOffset(animatedOffset.value.roundToInt(), 0) }
                        .size(60.dp)
                        .background(Color.White, RoundedCornerShape(100))
                        .border(1.dp, AccentGreen01, RoundedCornerShape(100)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Glisser pour compléter")
                }
            }
        }

    }
}
